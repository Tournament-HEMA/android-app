package ru.mertsalovda.tournament.ui.board

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arbelkilani.clock.enumeration.TimeCounterState
import com.arbelkilani.clock.listener.TimeCounterListener
import ru.mertsalovda.tournament.R
import ru.mertsalovda.tournament.databinding.FrBoardBinding
import java.util.concurrent.TimeUnit

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val SOUND = R.raw.svist2

private val DURATION = TimeUnit.SECONDS.toMillis(120)

class BoardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mBinding: FrBoardBinding
    private lateinit var mViewModel: BoardViewModel

    private lateinit var mSoundPool: SoundPool
    private val mSoundId = 1
    private var mStreamId: Int = -1
    private var mute = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mViewModel = ViewModelProvider(requireActivity()).get(BoardViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FrBoardBinding.inflate(inflater)

        initSoundPool()
        initTimer()
        addListeners()
        observeViewModel()

        return mBinding.root
    }

    private fun initSoundPool() {
        mSoundPool = SoundPool(4, AudioManager.STREAM_MUSIC, 100)
        mSoundPool.load(context, SOUND, 1)
    }

    private fun addListeners() {
        mBinding.timer.setOnClickListener {
            when (mBinding.timer.timeCounterState) {
                TimeCounterState.stopped -> mBinding.timer.runTimeCounter(DURATION)
                TimeCounterState.paused -> mBinding.timer.resumeTimeCounter()
                TimeCounterState.running -> mBinding.timer.pauseTimeCounter()
            }
        }
        mBinding.timer.setOnLongClickListener {
            initTimer()
            true
        }
        mBinding.timer.setTimeCounterListener(object : TimeCounterListener {
            override fun onTimeCounterCompleted() {
                playSound()
            }

            override fun onTimeCounterStateChanged(timeCounterState: TimeCounterState) {
                playSound()
            }
        })

        mBinding.btnBluePlusOne.setOnClickListener { mViewModel.plusBluePoints(1) }
        mBinding.btnBluePlusTwo.setOnClickListener { mViewModel.plusBluePoints(2) }
        mBinding.btnBlueMinusOne.setOnClickListener { mViewModel.plusBluePoints(-1) }
        mBinding.btnBlueMinusTwo.setOnClickListener { mViewModel.plusBluePoints(-2) }

        mBinding.btnRedPlusOne.setOnClickListener { mViewModel.plusRedPoints(1) }
        mBinding.btnRedPlusTwo.setOnClickListener { mViewModel.plusRedPoints(2) }
        mBinding.btnRedMinusOne.setOnClickListener { mViewModel.plusRedPoints(-1) }
        mBinding.btnRedMinusTwo.setOnClickListener { mViewModel.plusRedPoints(-2) }

        mBinding.btnMutualPlus.setOnClickListener { mViewModel.addMutual(1) }
        mBinding.btnMutualPlus.setOnLongClickListener {
            mViewModel.addMutual(-1)
            true
        }
    }

    private fun initTimer() {
        mute = true
        mBinding.timer.apply {
            runTimeCounter(DURATION)
            pauseTimeCounter()
        }
        mute = false
    }

    private fun playSound() {
        val leftVolume = 1f
        val rightVolume = 1f
        val priority = 1
        val loop = 0
        val normalPlaybackRate = 1f
        if (!mute) {
            mStreamId =
                mSoundPool.play(
                    mSoundId,
                    leftVolume,
                    rightVolume,
                    priority,
                    loop,
                    normalPlaybackRate
                )
        }
    }

    private fun observeViewModel() {
        mViewModel.redPoints.observe(viewLifecycleOwner, Observer {
            mBinding.tvRedCount.text = it.toString()
        })
        mViewModel.bluePoints.observe(viewLifecycleOwner, Observer {
            mBinding.tvBlueCount.text = it.toString()
        })

        mViewModel.winner.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        })

        mViewModel.lose.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Техническое поражение", Toast.LENGTH_LONG).show()
            }
        })

        mViewModel.mutual.observe(viewLifecycleOwner, Observer { addMutualOnLayout(it) })
    }

    private fun addMutualOnLayout(count: Int) {
        mBinding.mutualContainer.removeAllViewsInLayout()
        if (count > 0) {
            for (i in 1..count) {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_mutual, mBinding.root, false)
                mBinding.mutualContainer.addView(view)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BoardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}