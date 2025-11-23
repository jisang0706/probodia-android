# 프로바디아 (Probodia) - 당뇨 관리 앱

## 📱 개요
**프로바디아**는 당뇨 환자를 위한 종합 건강 관리 Android 앱입니다. 혈당, 혈압, 투약, 음식 기록과 AI 기반 음식 인식을 통해 체계적인 건강 관리를 지원합니다.

## 🏗️ 아키텍처
- **언어**: Kotlin
- **아키텍처**: MVVM + Repository Pattern
- **UI**: Data Binding, ViewPager2, Bottom Navigation
- **네트워킹**: Retrofit2 + Gson
- **데이터베이스**: Room
- **비동기 처리**: Coroutines + Flow
- **의존성 주입**: ViewModelFactory

## 🔑 핵심 기능

### 📊 건강 기록 관리
- **혈당 기록**: 시간대별 혈당 수치 기록 및 분석
- **혈압 기록**: 수축기/이완기 혈압 및 심박수 기록
- **투약 기록**: 약물 복용 이력 관리
- **음식 기록**: 식사 내용 및 탄수화물 정보 기록

### 🤖 AI 기반 기능
- **음식 인식**: 카메라로 촬영한 음식 자동 인식
- **혈당 예측**: AI 서버를 통한 혈당 수치 예측
- **영양소 분석**: 음식별 탄수화물 및 영양소 정보 제공

### 📈 데이터 분석
- **차트 시각화**: MPAndroidChart를 활용한 건강 데이터 그래프
- **기간별 분석**: 일/주/월 단위 건강 지표 분석
- **헤모글로빈 추적**: 당화혈색소 수치 모니터링

## 🛠️ 기술 스택

### 프론트엔드
- **Android SDK**: API 29-31
- **UI Components**: Material Design, ConstraintLayout
- **이미지 처리**: CircleImageView, 카메라 연동
- **차트**: MPAndroidChart
- **키보드**: KeyboardVisibilityEvent

### 백엔드 연동
- **인증**: Kakao Login SDK
- **클라우드**: AWS Cognito, S3
- **모니터링**: Firebase Analytics, Crashlytics
- **API**: RESTful API (Retrofit2)

### 데이터 관리
- **로컬 DB**: Room (SQLite)
- **네트워크**: OkHttp, SSL 인증서
- **보안**: Android Security Crypto

## 📁 프로젝트 구조

```
app/src/main/java/com/piri/probodia/
├── adapter/          # RecyclerView 어댑터들
├── data/
│   ├── db/          # Room 데이터베이스
│   └── remote/      # API 서비스 및 모델
├── repository/      # 데이터 레포지토리
├── view/
│   ├── activity/    # 액티비티들
│   ├── fragment/    # 프래그먼트들
│   └── layout/      # 커스텀 뷰
├── viewmodel/       # MVVM 뷰모델들
└── widget/          # 유틸리티 및 위젯
```

## 🔧 주요 의존성

```gradle
// UI & Navigation
implementation 'androidx.viewpager2:viewpager2:1.0.0'
implementation 'com.google.android.material:material:1.6.1'

// Network
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.6.4'

// Database
implementation 'androidx.room:room-runtime:2.4.0'
kapt 'androidx.room:room-compiler:2.4.0'

// Async
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0'

// Charts
implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'

// Auth
implementation 'com.kakao.sdk:v2-user:2.11.0'

// Cloud
implementation 'com.amazonaws:aws-android-sdk-mobile-client:2.13.5'
implementation 'com.google.firebase:firebase-analytics'
```

## 🚀 빌드 및 실행

1. **환경 설정**
   ```bash
   # local.properties 파일에 API 키 설정
   kakao_native_app_key=your_kakao_key
   server_url=your_server_url
   ai_server_url=your_ai_server_url
   ```

2. **빌드**
   ```bash
   ./gradlew assembleDebug
   ```

3. **실행**
   - Android Studio에서 실행
   - 최소 SDK: 29 (Android 10)
   - 타겟 SDK: 31 (Android 12)

## 📱 주요 화면

- **메인**: Bottom Navigation (기록/챌린지/커뮤니티/기타)
- **기록**: 혈당/혈압/투약/음식 기록 및 분석
- **커뮤니티**: 사용자 간 소통

## 🔐 보안 및 권한

- **인터넷**: API 통신
- **카메라**: 음식 인식
- **네트워크 보안**: SSL 인증서 검증
- **데이터 암호화**: Android Security Crypto

## 📊 버전 정보

- **현재 버전**: 1.3.1 (Build 24)
- **최소 Android**: 10 (API 29)
- **타겟 Android**: 12 (API 31)

---

**프로바디아**는 당뇨 환자의 일상적인 건강 관리를 돕는 종합 솔루션입니다.
