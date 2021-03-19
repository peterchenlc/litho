/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.litho;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
//import androidx.annotation.Nullable;
import com.facebook.litho.drawable.DrawableUtils;

class DrawableComponent<T extends Drawable> extends Component {

  private static final String SIMPLE_NAME = "DrawableComponent";

  Drawable mDrawable;
  int mDrawableWidth;
  int mDrawableHeight;

  private DrawableComponent(Drawable drawable) {
    super(SIMPLE_NAME);
    mDrawable = drawable;
  }

  @Override
  protected void onBoundsDefined(ComponentContext c, ComponentLayout layout) {
    setDrawableWidth(layout.getWidth());
    setDrawableHeight(layout.getHeight());
  }

  @Override
  protected Object onCreateMountContent(Context c) {
    return new MatrixDrawable();
  }

  @Override
  protected void onMount(ComponentContext context, Object content) {
    MatrixDrawable drawable = (MatrixDrawable) content;

    drawable.mount(getDrawable());
  }

  @Override
  protected void onBind(ComponentContext c, Object mountedContent) {
    final MatrixDrawable mountedDrawable = (MatrixDrawable) mountedContent;

    mountedDrawable.bind(getDrawableWidth(), getDrawableHeight());
  }

  @Override
  void bind(ComponentContext c, Object mountedContent) {
    final boolean isTracing = ComponentsSystrace.isTracing();
    if (isTracing) {
      ComponentsSystrace.beginSection("onBind:" + SIMPLE_NAME);
    }
    try {
      onBind(c, mountedContent);
    } finally {
      if (isTracing) {
        ComponentsSystrace.endSection();
      }
    }
  }

  @Override
  void mount(ComponentContext c, Object mountedContent) {
    final boolean isTracing = ComponentsSystrace.isTracing();
    if (isTracing) {
      ComponentsSystrace.beginSection("onMount:" + SIMPLE_NAME);
    }
    try {
      onMount(c, mountedContent);
    } finally {
      if (isTracing) {
        ComponentsSystrace.endSection();
      }
    }
  }

  @Override
  protected void onUnmount(ComponentContext context, Object mountedContent) {
    final MatrixDrawable<T> matrixDrawable = (MatrixDrawable<T>) mountedContent;
    matrixDrawable.unmount();
  }

  @Override
  protected boolean isPureRender() {
    return true;
  }

  @Override
  public MountType getMountType() {
    return MountType.DRAWABLE;
  }

  public static DrawableComponent create(Drawable drawable) {
    return new DrawableComponent<>(drawable);
  }

  @Override
  protected boolean shouldUpdate(
      final @Nullable Component previous,
      final @Nullable StateContainer previousStateContainer,
      final @Nullable Component next,
      final @Nullable StateContainer nextStateContainer) {
    final Drawable previousDrawable =
        previous == null ? null : ((DrawableComponent) previous).getDrawable();
    final Drawable nextDrawable = next == null ? null : ((DrawableComponent) next).getDrawable();

    return !DrawableUtils.isEquivalentTo(previousDrawable, nextDrawable);
  }

  private Drawable getDrawable() {
    return mDrawable;
  }

  @Override
  public boolean isEquivalentTo(Component o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DrawableComponent drawableComponent = (DrawableComponent) o;

    return DrawableUtils.isEquivalentTo(mDrawable, drawableComponent.mDrawable);
  }

  private void setDrawableWidth(int drawableWidth) {
    mDrawableWidth = drawableWidth;
  }

  private int getDrawableWidth() {
    return mDrawableWidth;
  }

  private void setDrawableHeight(int drawableHeight) {
    mDrawableHeight = drawableHeight;
  }

  private int getDrawableHeight() {
    return mDrawableHeight;
  }
}
