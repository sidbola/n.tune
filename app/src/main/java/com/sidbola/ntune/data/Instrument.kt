package com.sidbola.ntune.data

import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.content.ContextCompat
import com.sidbola.ntune.NTuneApp
import com.sidbola.ntune.R

enum class Instrument {
    GUITAR {
        override val getImage: Drawable
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NTuneApp.getApplicationContext().getDrawable(R.drawable.ic_guitar)
            } else {
                ContextCompat.getDrawable(NTuneApp.getApplicationContext(), R.drawable.ic_guitar)!!
            }
        override val availableTunings: Array<Tuning>
            get() = arrayOf(
                Tuning("Standard", arrayOf(Note.E_2, Note.A_2, Note.D_3, Note.G_3, Note.B_3, Note.E_4)),
                Tuning("Drop D", arrayOf(Note.D_2, Note.A_2, Note.D_3, Note.G_3, Note.B_3, Note.E_4)),
                Tuning("Double Drop D", arrayOf(Note.D_2, Note.A_2, Note.D_3, Note.G_3, Note.B_3, Note.D_4)),
                Tuning("Open D", arrayOf(Note.D_2, Note.A_2, Note.D_3, Note.F_SHARP_3, Note.A_3, Note.D_4)),
                Tuning("Open G", arrayOf(Note.D_2, Note.G_2, Note.D_3, Note.G_3, Note.B_3, Note.D_4)),
                Tuning("Modal D", arrayOf(Note.D_2, Note.A_2, Note.D_3, Note.G_3, Note.A_3, Note.D_4))
            )


    },
    //Bass strings are (5th string) B0=30.87Hz, (4th string) E1=41.20Hz, A1=55Hz, D2=73.42Hz, G2=98Hz
    BASS_FOUR_STRING {
        override val getImage: Drawable
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NTuneApp.getApplicationContext().getDrawable(R.drawable.ic_bass_four)
            } else {
                ContextCompat.getDrawable(NTuneApp.getApplicationContext(), R.drawable.ic_guitar)!!

            }
        override val availableTunings: Array<Tuning>
            get() = arrayOf(
                Tuning("Standard", arrayOf(Note.E_1, Note.A_1, Note.D_2, Note.G_2))
            )

        override fun toString(): String {
            return "BASS 4"
        }
    },
    BASS_FIVE_STRING {
        override val getImage: Drawable
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NTuneApp.getApplicationContext().getDrawable(R.drawable.ic_bass_five)
            } else {
                ContextCompat.getDrawable(NTuneApp.getApplicationContext(), R.drawable.ic_guitar)!!

            }
        override val availableTunings: Array<Tuning>
            get() = arrayOf(
                Tuning("Standard", arrayOf(Note.B_0, Note.E_1, Note.A_1, Note.D_2, Note.G_2))
            )

        override fun toString(): String {
            return "BASS 5"
        }
    },
    // Mandolin & violin strings are G3=196Hz, D4=293.7Hz, A4=440Hz, E5=659.3Hz
    VIOLIN {
        override val getImage: Drawable
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NTuneApp.getApplicationContext().getDrawable(R.drawable.ic_violin)
            } else {
                ContextCompat.getDrawable(NTuneApp.getApplicationContext(), R.drawable.ic_guitar)!!

            }
        override val availableTunings: Array<Tuning>
            get() = arrayOf(
                Tuning("Standard", arrayOf(Note.G_3, Note.D_4, Note.A_4, Note.E_5))
            )
    },
    MANDOLIN {
        override val getImage: Drawable
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NTuneApp.getApplicationContext().getDrawable(R.drawable.ic_mandolin)
            } else {
                ContextCompat.getDrawable(NTuneApp.getApplicationContext(), R.drawable.ic_guitar)!!

            }
        override val availableTunings: Array<Tuning>
            get() = arrayOf(
                Tuning("Standard", arrayOf(Note.G_3, Note.D_4, Note.A_4, Note.E_5))
            )
    },
    // Viola & tenor banjo strings are C3=130.8Hz, G3=196Hz, D4=293.7Hz, A4=440Hz
    VIOLA {
        override val getImage: Drawable
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NTuneApp.getApplicationContext().getDrawable(R.drawable.ic_viola)
            } else {
                ContextCompat.getDrawable(NTuneApp.getApplicationContext(), R.drawable.ic_guitar)!!

            }
        override val availableTunings: Array<Tuning>
            get() = arrayOf(
                Tuning("Standard", arrayOf(Note.C_3, Note.G_3, Note.D_4, Note.A_4))
            )
    },
    BANJO {
        override val getImage: Drawable
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NTuneApp.getApplicationContext().getDrawable(R.drawable.ic_banjo)
            } else {
                ContextCompat.getDrawable(NTuneApp.getApplicationContext(), R.drawable.ic_guitar)!!

            }
        override val availableTunings: Array<Tuning>
            get() = arrayOf(
                Tuning("Standard", arrayOf(Note.C_3, Note.G_3, Note.D_4, Note.A_4))
            )
    },
    // Cello strings are C2=65.41Hz, G2=98Hz, D3=146.8Hz, A3=220Hz
    CELLO {
        override val getImage: Drawable
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NTuneApp.getApplicationContext().getDrawable(R.drawable.ic_cello)
            } else {
                ContextCompat.getDrawable(NTuneApp.getApplicationContext(), R.drawable.ic_guitar)!!

            }
        override val availableTunings: Array<Tuning>
            get() = arrayOf(
                Tuning("Standard", arrayOf(Note.C_2, Note.G_2, Note.D_3, Note.A_3))
            )
    };

    abstract val getImage: Drawable
    abstract val availableTunings: Array<Tuning>
}