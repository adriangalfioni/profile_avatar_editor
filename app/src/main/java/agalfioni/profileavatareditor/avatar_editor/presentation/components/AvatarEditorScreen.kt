package agalfioni.profileavatareditor.avatar_editor.presentation.components

import agalfioni.profileavatareditor.R
import agalfioni.profileavatareditor.avatar_editor.data.StorageUtil
import agalfioni.profileavatareditor.core.navigation.ResultStore
import agalfioni.profileavatareditor.ui.theme.PlusJakartaSans
import agalfioni.profileavatareditor.ui.theme.ProfileAvatarEditorTheme
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarEditorScreen(
    imageUri: Uri,
    modifier: Modifier = Modifier,
    onImageCropped: () -> Unit = {},
    resultStore: ResultStore? = null,
) {
    val navigationIconSize = 40.dp

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                ),
                navigationIcon = {
                    Button(
                        onClick = { onImageCropped() },
                        modifier= Modifier.size(navigationIconSize),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp),  //avoid the little icon
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.inverseSurface,
                            contentColor = colorResource(R.color.white)
                        )
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                },
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = navigationIconSize), // compensate nav icon
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Edit Avatar",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                },
                windowInsets = WindowInsets.statusBars.add(WindowInsets(top = 32.dp)),
            )
        }
    ) { innerPadding ->

        var cropRect by remember { mutableStateOf(Rect.Zero) }
        var containerSize by remember { mutableStateOf(Size.Zero) }
        val scope = rememberCoroutineScope() // Needed to run background tasks
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.onSurface)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            
            ImageWithCropArea(
                imageUri = imageUri,
                onCropRectChanged = { rect, size ->
                    cropRect = rect
                    containerSize = size
                }
            )
            
            Spacer(modifier = Modifier.height(80.dp))
            
            PrimaryButton(
                text = "Save",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 66.dp),
                onClick = {
                    if (containerSize != Size.Zero && cropRect != Rect.Zero) {
                        val bitmap = cropBitmap(context, imageUri, cropRect, containerSize)
                        if (bitmap != null) {
                            // Save to disk in background
                            scope.launch {
                                val savedPath = StorageUtil.saveToInternalStorage(context, bitmap)
                                StorageUtil.savePathToPreferences(context, savedPath)

                                // Navigate away
                                resultStore?.setResult("cropped_image", bitmap)
                                onImageCropped()
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ImageWithCropArea(
    imageUri: Uri,
    onCropRectChanged: (Rect, Size) -> Unit,
    modifier: Modifier = Modifier
) {
    var parentSize by remember { mutableStateOf(Size.Zero) }
    var cropRect by remember { mutableStateOf(Rect.Zero) }
    var isInitialized by remember { mutableStateOf(false) }

    // Define the shadow color (e.g., 60% opaque black)
    val overlayColor = Color.Black.copy(alpha = 0.7f)

    val density = LocalDensity.current

    Box(
        modifier = modifier
            .size(380.dp)
            .padding(16.dp)
            .onGloballyPositioned { coordinates ->
                parentSize = coordinates.size.toSize()

                if (!isInitialized && parentSize != Size.Zero) {
                    val initialSide = min(parentSize.width, parentSize.height) * 0.6f
                    cropRect = Rect(
                        offset = Offset(
                            (parentSize.width - initialSide) / 2f,
                            (parentSize.height - initialSide) / 2f
                        ),
                        size = Size(initialSide, initialSide)
                    )
                    isInitialized = true
                    onCropRectChanged(cropRect, parentSize)
                }
            }
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // DARK OVERLAY CANVAS
        // This draws the shadow everywhere EXCEPT inside the cropRect
        if (isInitialized) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // 1. Draw Top Rect
                drawRect(
                    color = overlayColor,
                    topLeft = Offset(0f, 0f),
                    size = Size(parentSize.width, cropRect.top)
                )
                // 2. Draw Bottom Rect
                drawRect(
                    color = overlayColor,
                    topLeft = Offset(0f, cropRect.bottom),
                    size = Size(parentSize.width, parentSize.height - cropRect.bottom)
                )
                // 3. Draw Left Rect (between top and bottom)
                drawRect(
                    color = overlayColor,
                    topLeft = Offset(0f, cropRect.top),
                    size = Size(cropRect.left, cropRect.height)
                )
                // 4. Draw Right Rect (between top and bottom)
                drawRect(
                    color = overlayColor,
                    topLeft = Offset(cropRect.right, cropRect.top),
                    size = Size(parentSize.width - cropRect.right, cropRect.height)
                )
            }
        }

        if (isInitialized) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            cropRect.left.roundToInt(),
                            cropRect.top.roundToInt()
                        )
                    }
                    .size(with(density) { cropRect.width.toDp() })
                    .border(width = 0.5.dp, color = Color.White)
                    .cornerBorder(color = Color.White)
                    .pointerInput(Unit) {
                        detectTransformGestures { centroid, pan, zoom, _ ->
                            val oldWidth = cropRect.width
                            val newWidth = (oldWidth * zoom).coerceIn(
                                minimumValue = 100f,
                                maximumValue = min(parentSize.width, parentSize.height)
                            )

                            val sizeChange = newWidth - oldWidth
                            val zoomOffsetAdjustmentX = (centroid.x / oldWidth) * sizeChange
                            val zoomOffsetAdjustmentY = (centroid.y / oldWidth) * sizeChange

                            var newLeft = cropRect.left + pan.x - zoomOffsetAdjustmentX
                            var newTop = cropRect.top + pan.y - zoomOffsetAdjustmentY

                            newLeft = newLeft.coerceIn(0f, parentSize.width - newWidth)
                            newTop = newTop.coerceIn(0f, parentSize.height - newWidth)

                            cropRect = Rect(
                                offset = Offset(newLeft, newTop),
                                size = Size(newWidth, newWidth)
                            )
                            onCropRectChanged(cropRect, parentSize)
                        }
                    }
            ) {
                NineAreaCircleGrid(
                    modifier = Modifier.fillMaxSize(),
                    lineColor = Color.White,
                    lineWidth = 0.8.dp,
                    circleBorderWidth = 0.8.dp
                )
            }
        }
    }
}

private fun cropBitmap(
    context: Context,
    uri: Uri,
    cropRect: Rect,
    containerSize: Size
): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream) ?: return null
        
        // 1. Calculate the actual displayed size of the image within the container (ContentScale.Crop)
        val containerAspect = containerSize.width / containerSize.height
        val bitmapAspect = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
        
        val scale: Float
        val offsetX: Float
        val offsetY: Float
        
        if (bitmapAspect > containerAspect) {
            // Bitmap is wider than container
            scale = containerSize.height / originalBitmap.height.toFloat()
            offsetX = (originalBitmap.width.toFloat() * scale - containerSize.width) / 2f
            offsetY = 0f
        } else {
            // Bitmap is taller than container
            scale = containerSize.width / originalBitmap.width.toFloat()
            offsetX = 0f
            offsetY = (originalBitmap.height.toFloat() * scale - containerSize.height) / 2f
        }
        
        // 2. Map the cropRect from container coordinates to original bitmap coordinates
        val bitmapLeft = (cropRect.left + offsetX) / scale
        val bitmapTop = (cropRect.top + offsetY) / scale
        val bitmapWidth = cropRect.width / scale
        val bitmapHeight = cropRect.height / scale
        
        // 3. Perform the crop
        Bitmap.createBitmap(
            originalBitmap,
            bitmapLeft.roundToInt().coerceIn(0, originalBitmap.width - 1),
            bitmapTop.roundToInt().coerceIn(0, originalBitmap.height - 1),
            bitmapWidth.roundToInt().coerceAtMost(originalBitmap.width - bitmapLeft.roundToInt()),
            bitmapHeight.roundToInt().coerceAtMost(originalBitmap.height - bitmapTop.roundToInt())
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Preview
@Composable
private fun AvatarEditorScreenPreview() {
    ProfileAvatarEditorTheme {
        AvatarEditorScreen(Uri.EMPTY)
    }
}
