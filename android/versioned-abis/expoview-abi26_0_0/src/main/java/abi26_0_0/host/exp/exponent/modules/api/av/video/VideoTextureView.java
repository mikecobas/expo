package abi26_0_0.host.exp.exponent.modules.api.av.video;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.util.Pair;
import android.view.Surface;
import android.view.TextureView;

import abi26_0_0.com.facebook.react.uimanager.ThemedReactContext;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScaleManager;
import com.yqritc.scalablevideoview.Size;

@SuppressLint("ViewConstructor")
public class VideoTextureView extends TextureView implements TextureView.SurfaceTextureListener {

  private VideoView mVideoView = null;

  private boolean mIsAttachedToWindow = false;

  private Surface mSurface = null;

  public VideoTextureView(final ThemedReactContext themedReactContext, VideoView videoView) {
    super(themedReactContext, null, 0);

    mVideoView = videoView;

    setSurfaceTextureListener(this);
  }

  public boolean isAttachedToWindow() {
    return mIsAttachedToWindow;
  }

  public Surface getSurface() {
    return mSurface;
  }

  public void scaleVideoSize(final Pair<Integer, Integer> videoWidthHeight, ScalableType resizeMode) {
    final int videoWidth = videoWidthHeight.first;
    final int videoHeight = videoWidthHeight.second;

    if (videoWidth == 0 || videoHeight == 0) {
      return;
    }

    final Size viewSize = new Size(getWidth(), getHeight());
    final Size videoSize = new Size(videoWidth, videoHeight);
    final Matrix matrix = new ScaleManager(viewSize, videoSize).getScaleMatrix(resizeMode);
    if (matrix != null) {
      setTransform(matrix);
    }
  }

  // TextureView

  @Override
  public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int width, final int height) {
    mSurface = new Surface(surfaceTexture);
    mVideoView.tryUpdateVideoSurface(mSurface);
  }

  @Override
  public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int width, final int height) {
    // no-op
  }

  @Override
  public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
    // no-op
  }

  @Override
  public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
    mSurface = null;
    mVideoView.tryUpdateVideoSurface(null);
    return true;
  }

  // View

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    mIsAttachedToWindow = false;
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    mIsAttachedToWindow = true;
    mVideoView.tryUpdateVideoSurface(mSurface);
  }
}
