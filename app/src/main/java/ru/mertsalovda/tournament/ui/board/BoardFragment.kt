package ru.mertsalovda.tournament.ui.board

import android.content.Context
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
import ru.mertsalovda.tournament.databinding.FrBoardBinding
import java.util.concurrent.TimeUnit


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BoardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mBinding: FrBoardBinding
    private lateinit var mViewModel: BoardViewModel

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

        addListeners()
        observeViewModel()

        return mBinding.root
    }

    private fun addListeners() {
        mBinding.timer.setTimeCounterListener(object : TimeCounterListener {
            override fun onTimeCounterCompleted() {
                Toast.makeText(context, "END", Toast.LENGTH_LONG).show()
            }

            override fun onTimeCounterStateChanged(timeCounterState: TimeCounterState) {
                Toast.makeText(context, timeCounterState.name, Toast.LENGTH_LONG).show()
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