package ru.mertsalovda.tournament.ui.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.arbelkilani.clock.enumeration.TimeCounterState
import com.arbelkilani.clock.listener.TimeCounterListener
import ru.mertsalovda.tournament.data.model.Rule
import ru.mertsalovda.tournament.databinding.FrBoardBinding
import java.util.concurrent.TimeUnit


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BoardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mBinding: FrBoardBinding
    private val mRule = Rule()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FrBoardBinding.inflate(inflater)

        val time = TimeUnit.SECONDS.toMillis(10)
        mBinding.timer.apply {
            runTimeCounter(time)
            pauseTimeCounter()
        }

        mBinding.timer.setOnClickListener {
            when (mBinding.timer.timeCounterState) {
                TimeCounterState.stopped -> mBinding.timer.runTimeCounter(time)
                TimeCounterState.paused -> mBinding.timer.resumeTimeCounter()
                TimeCounterState.running -> mBinding.timer.pauseTimeCounter()
            }
        }

        mBinding.timer.setTimeCounterListener(object : TimeCounterListener {
            override fun onTimeCounterCompleted() {
                Toast.makeText(context, "END", Toast.LENGTH_LONG).show()
            }

            override fun onTimeCounterStateChanged(timeCounterState: TimeCounterState) {
                Toast.makeText(context, timeCounterState.name, Toast.LENGTH_LONG).show()
            }

        })

        mBinding.btnBluePlusOne.setOnClickListener { plusPoints(mBinding.tvBlueCount, 1) }
        mBinding.btnBluePlusTwo.setOnClickListener { plusPoints(mBinding.tvBlueCount, 2) }
        mBinding.btnBlueMinusOne.setOnClickListener { minusPoints(mBinding.tvBlueCount, 1) }
        mBinding.btnBlueMinusTwo.setOnClickListener { minusPoints(mBinding.tvBlueCount, 2) }

        mBinding.btnRedPlusOne.setOnClickListener { plusPoints(mBinding.tvRedCount, 1) }
        mBinding.btnRedPlusTwo.setOnClickListener { plusPoints(mBinding.tvRedCount, 2) }
        mBinding.btnRedMinusOne.setOnClickListener { minusPoints(mBinding.tvRedCount, 1) }
        mBinding.btnRedMinusTwo.setOnClickListener { minusPoints(mBinding.tvRedCount, 2) }

        return mBinding.root
    }

    private fun plusPoints(textView: TextView, points: Int) {
        val currentPoints = textView.text.toString().toInt()
        textView.text = (currentPoints + points).toString()
    }

    private fun minusPoints(textView: TextView, points: Int) {
        val currentPoints = textView.text.toString().toInt()
        if (currentPoints - points < 0) {
            textView.text = "0"
        } else {
            textView.text = (currentPoints - points).toString()
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