package com.piri.probodia.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChallengeDto (
    // 챌린지 id
    val id : Int,

    // 혈당기록, 음식기록
    val type : String,

    // 관리자가 설정한 챌린지인지
    val official : Boolean,

    // 챌린지 이름
    val name : String,

    // 챌린지 내용
    val content : String,

    // 챌린지 완료시 포인트
    val earnPoint : Int,

    // 참가자 수
    val totalCnt : Int,

    // 시작일
    val stDate : String,

    // 종료일
    val enDate : String,

    // 챌린지 진행 정보
    val frequency : Frequency,

    // 주의사항
    val caution : MutableList<String>,
) : Parcelable {

    @Parcelize
    data class Frequency (
        // 주기 (년, 월, 주, 일)
        val dateType : String,

        // 인증 간격
        val period : Int,

        // 횟수
        val times : Int
            ) : Parcelable
}
