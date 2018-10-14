package com.sidbola.ntune.model

enum class Instrument {
    GUITAR {
        override fun getNotePitchFrequencies(): Array<NotePitchInfo> {
            return arrayOf(
                NotePitchInfo("E", 82.41f, 1),
                NotePitchInfo("A", 110f, 2),
                NotePitchInfo("D", 146.8f, 3),
                NotePitchInfo("G", 196f, 4),
                NotePitchInfo("B", 246.9f, 5),
                NotePitchInfo("E", 329.6f, 6)
            )
        }
    },
    BASS_FOUR_STRING {
        override fun getNotePitchFrequencies(): Array<NotePitchInfo> {
            return Array(4) {
                NotePitchInfo("E", 41.20f, 2)
                NotePitchInfo("A", 55f, 3)
                NotePitchInfo("D", 73.42f, 4)
                NotePitchInfo("G", 98f, 5)
            }
        }
    },
    BASS_FIVE_STRING {
        override fun getNotePitchFrequencies(): Array<NotePitchInfo> {
            return Array(5) {
                NotePitchInfo("B", 30.87f, 1)
                NotePitchInfo("E", 41.20f, 2)
                NotePitchInfo("A", 55f, 3)
                NotePitchInfo("D", 73.42f, 4)
                NotePitchInfo("G", 98f, 5)
            }
        }
    },
    VIOLIN {
        override fun getNotePitchFrequencies(): Array<NotePitchInfo> {
            return Array(4) {
                NotePitchInfo("G", 196f, 1)
                NotePitchInfo("D", 293.7f, 2)
                NotePitchInfo("A", 440f, 3)
                NotePitchInfo("E", 659.3f, 4)
            }
        }
    },
    MANDOLIN {
        override fun getNotePitchFrequencies(): Array<NotePitchInfo> {
            return Array(4) {
                NotePitchInfo("G", 196f, 1)
                NotePitchInfo("D", 293.7f, 2)
                NotePitchInfo("A", 440f, 3)
                NotePitchInfo("E", 659.3f, 4)
            }
        }
    },
    VIOLA {
        override fun getNotePitchFrequencies(): Array<NotePitchInfo> {
            return Array(4) {
                NotePitchInfo("C", 130.8f, 1)
                NotePitchInfo("G", 196f, 2)
                NotePitchInfo("D", 293.7f, 3)
                NotePitchInfo("A", 440f, 4)
            }
        }
    },
    BANJO {
        override fun getNotePitchFrequencies(): Array<NotePitchInfo> {
            return Array(4) {
                NotePitchInfo("C", 130.8f, 1)
                NotePitchInfo("G", 196f, 2)
                NotePitchInfo("D", 293.7f, 3)
                NotePitchInfo("A", 440f, 4)
            }
        }
    },
    CELLO {
        override fun getNotePitchFrequencies(): Array<NotePitchInfo> {
            return Array(4) {
                NotePitchInfo("C", 65.41f, 1)
                NotePitchInfo("G", 98f, 2)
                NotePitchInfo("D", 146.8f, 3)
                NotePitchInfo("A", 220f, 4)
            }        }
    };

    abstract fun getNotePitchFrequencies(): Array<NotePitchInfo>
}

data class NotePitchInfo(
    val note: String,
    val freq: Float,
    val index: Int
)