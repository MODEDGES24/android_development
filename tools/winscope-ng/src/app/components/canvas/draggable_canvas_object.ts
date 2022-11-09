/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { MathUtils } from "three/src/Three";
import { Segment } from "../timeline/utils";
import { CanvasDrawer } from "./canvas_drawer";

export type drawConfig = {
  fillStyle: string,
  fill: boolean
}

export class DraggableCanvasObject {
  private draggingPosition: number|undefined;

  constructor(
    private drawer: CanvasDrawer,
    private positionGetter: () => number,
    private definePathFunc: (ctx: CanvasRenderingContext2D, position: number) => void,
    private drawConfig: drawConfig,
    private onDrag: (x: number) => void,
    private onDrop: (x: number) => void,
    private rangeGetter: () => Segment,
  ) {
    this.drawer.handler.registerDraggableObject(this, (x, ) => {
      this.draggingPosition = this.clampPositionToRange(x);
      this.onDrag(this.draggingPosition);
      this.drawer.draw();
    }, (x: number, ) => {
      this.draggingPosition = undefined;
      this.onDrop(this.clampPositionToRange(x));
      this.drawer.draw();
    });
  }

  get range(): Segment {
    return this.rangeGetter();
  }

  get position(): number {
    return this.draggingPosition !== undefined ? this.draggingPosition : this.positionGetter();
  }

  public definePath(ctx: CanvasRenderingContext2D) {
    this.definePathFunc(ctx, this.position);
  }

  public draw(ctx: CanvasRenderingContext2D) {
    this.doDraw(ctx);
    this.drawer.handler.notifyDrawnOnTop(this);
  }

  private doDraw(ctx: CanvasRenderingContext2D) {
    this.definePath(ctx);
    ctx.fillStyle = this.drawConfig.fillStyle;
    if (this.drawConfig.fill) {
      ctx.fill();
    }
  }

  private clampPositionToRange(x: number): number {
    return MathUtils.clamp(x, this.range.from, this.range.to);
  }
}