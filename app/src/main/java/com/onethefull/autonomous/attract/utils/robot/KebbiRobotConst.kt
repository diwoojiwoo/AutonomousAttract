package com.onethefull.dasomiconv.utils.robot

/**
 * Created by Douner on 2023/03/27.
 */
object KebbiRobotConst {
    const val ON_PIR = "onPIR"
    const val ON_TAP = "onTap"
    const val ON_LONG_PRESS = "onLongPress"
    const val ON_DROP_SENSER = "onDropEvent"

    const val ON_VOLUME_HIGH = "onVolumeHigh"
    const val ON_VOLUME_LOW = "onVolumeLow"
    const val ON_TOUCH_PARAM = "TYPE"

    const val POSITION_HEAD = 1
    const val POSITION_CHEST = 2
    const val POSITION_RIGHT_HAND = 3
    const val POSITION_LEFT_HAND = 4
    const val POSITION_LEFT_FACE = 5
    const val POSITION_RIGHT_FACE = 6

    const val ACTION_KEBBI_SEND_FACE_OWNER = "ACTION_KEBBI_SEND_FACE_OWNER"
    const val PRAM_SEND_FACE_OWNER_INFO = "PRAM_SEND_FACE_OWNER_INFO"


    val motionList = arrayOf(
//        "665_Alarm_ChickenMusic",
        "666_BA_LookU036",
        "666_BA_RArmR90",
        "666_BA_RElbowR",
        "666_BA_RElbowS",
        "666_BA_RHandR01",
        "666_BA_RHandR02",
        "666_BA_RHandR03",
        "666_BA_RzArmL45",
        "666_BA_RzArmL90",
        "666_BA_RzArmS45",
        "666_BA_TurnHead",
        "666_BA_TurnL360",
        "666_BA_TurnR180",
        "666_BA_TurnR360",
        "666_DA_Applaud",
        "666_DA_Eat",
        "666_DA_Hit",
        "666_DA_Listen",
        "666_DA_PickUp",
        "666_DA_PushFast",
        "666_DA_PushSlowly",
        "666_DA_Putdown",
        "666_DA_Scratching",
        "666_DA_SearchF",
        "666_EM_Blush",
        "666_EM_Curse",
        "666_EM_Disdain",
        "666_EM_Fear01",
        "666_EM_Fear02",
        "666_EM_Fear03",
        "666_EM_Happy01",
        "666_EM_Happy02",
        "666_EM_Happy03",
        "666_EM_Perverse",
        "666_EM_Sad02",
        "666_EM_Sad03",
        "666_EM_Sad04",
        "666_EM_Shameless",
        "666_FI_Kungfu01",
        "666_FI_Surrender",
        "666_IM_Bird",
        "666_IM_Mario",
        "666_IM_Rooster",
        "666_LE_ListenSong",
        "666_LE_Read",
        "666_PE_Abuse",
        "666_PE_Dance02",
        "666_PE_Harmonica",
        "666_PE_Hercules",
        "666_PE_Hug",
        "666_PE_Killed",
        "666_PE_NijaRun",
//        "666_PE_OperaFace",
        "666_PE_PGuitar",
        "666_PE_PlayCello",
        "666_PE_PlayGuitar",
        "666_PE_Power",
        "666_PE_PushGlasses",
        "666_PE_Shrink",
        "666_PE_Singing",
        "666_PE_Triangel",
        "666_PE_WaveDance",
        "666_RE_Bow",
        "666_RE_Bye",
        "666_RE_Embrace",
        "666_RE_HiR",
        "666_RE_Request",
        "666_RE_Welcome",
        "666_SA_Discover",
        "666_SA_Shocked",
        "666_SA_Sick",
        "666_SP_Boat",
        "666_SP_Bowling",
        "666_SP_Chest",
        "666_SP_HorizontalBar",
        "666_SP_Rope",
        "666_SP_Run",
        "666_SP_Shot",
        "666_SP_Swim",
        "666_TA_LookDnU",
        "666_TA_LookLR",
        "666_TA_LookRL",
        "666_TA_LookUnD",
        "666_TA_Roar",
        "666_TA_TalkL",
        "666_TA_TalkR",
        "666_TA_TalkS",
        "666_TA_TalkY",
        "666_TA_YoR",
        "666_WO_Drive",
        "666_WO_Traffic"
//        "667_BBQ_Default",
//        "667_BBQ_HammerBelly",
//        "667_FD_ChoicePlayer",
//        "667_FD_NG",
//        "667_FD_Totorial",
//        "667_MG_HandsUp",
//        "667_NW_PauseGo",
//        "667_NW_TickTuck",
//        "667_P4_Answer",
//        "667_P4_Answerplz",
//        "667_P4_BananaFall",
//        "667_P4_Bluewin_2",
//        "667_P4_Boxing",
//        "667_P4_CameraAction",
//        "667_P4_ChickRush",
//        "667_P4_Dinosaur",
//        "667_P4_EleShock",
//        "667_P4_Gift",
//        "667_P4_Guess",
//        "667_P4_Guitarcrash",
//        "667_P4_Hammer",
//        "667_P4_Harp",
//        "667_P4_Kite",
//        "667_P4_Makeface",
//        "667_P4_Piano",
//        "667_P4_Pingpong",
//        "667_P4_RCall",
//        "667_P4_Redwin_2",
//        "667_P4_Relay",
//        "667_P4_Robotwalk",
//        "667_P4_Shakethering",
//        "667_P4_Shootgun",
//        "667_P4_ShyConfess",
//        "667_P4_Sigh",
//        "667_P4_Start",
//        "667_P4_Surrender",
//        "667_P4_Sweep",
//        "667_P4_Taekwondo",
//        "667_P4_TakePhoto",
//        "667_P4_TakeShower",
//        "667_P4_Tie",
//        "667_P4_Train",
//        "667_P4_TugOfWar",
//        "667_P4_Tutorial",
//        "667_P4_Umbrella"
    )

    fun getKebbiMotionDA(): List<String> {
        return motionList.filter { motion ->
            motion.contains("666_DA")
        }
    }

    fun getKebbiMotionEM(): List<String> {
        return motionList.filter { motion ->
            motion.contains("666_EM")
        }
    }

    fun getKebbiMotionBA(): List<String> {
        return motionList.filter { motion ->
            motion.contains("666_BA")
        }
    }

    fun getBaseRandomMotion(): String {
        return motionList.filter { motion ->
            motion.contains("666_DA") || motion.contains("666_EM") || motion.contains("666_BA")
        }.random()
    }
}