# 포물선 애니메이션 및 크기 조정

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


이 프로젝트는 **Jetpack Compose**를 사용하여 **포물선 애니메이션**과 **크기 조정**을 결합한 예제입니다.

이미지나 다른 컴포저블 요소가 시간에 따라 포물선 경로를 따라 이동하면서 크기도 변화하는 애니메이션을 구현합니다.

이 애니메이션은 동적 효과를 제공하며, 화면 크기와 방향에 맞게 반응합니다.

예제와 함께 Modifier 확장 함수를 제공합니다

## 기능

- **포물선 이동**: 요소가 포물선 경로를 따라 이동합니다. 포물선 그래프는 이차방정식을 사용하여 동작합니다
- **크기 애니메이션**: 요소가 크기를 변화시키며 이동합니다.
- **커스터마이즈 가능**: 애니메이션의 지속 시간, 시작 지연, 초기 크기, 목표 크기, 이동 방향 등을 제어할 수 있습니다.
- **반응형 디자인**: 화면 크기와 방향에 맞춰 자동으로 조정됩니다.


## 필수 조건

- Android Studio 4.2 이상 (또는 Android 개발에 선호하는 IDE)
- Kotlin 1.5 이상
- Jetpack Compose 1.0 이상


## 작동 예시 (GIF)

<img src="https://github.com/loyalflower0908/ParabolicScaleAnimation/blob/master/ParabolicScaleAnimation.gif" width="30%" height="30%">
