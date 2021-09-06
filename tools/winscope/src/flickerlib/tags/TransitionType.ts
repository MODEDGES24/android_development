/*
 * Copyright 2021, The Android Open Source Project
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

enum TransitionType {
  ROTATION = 'ROTATION',
  PIP_ENTER = 'PIP_ENTER',
  PIP_RESIZE ='PIP_RESIZE',
  PIP_EXIT = 'PIP_EXIT',
  APP_LAUNCH = 'APP_LAUNCH',
  APP_CLOSE = 'APP_CLOSE',
  IME_APPEAR = 'IME_APPEAR',
  IME_DISAPPEAR = 'IME_DISAPPEAR',
};

export default TransitionType;