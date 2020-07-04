package ru.mertsalovda.tournament.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arbelkilani.clock.Clock
import com.arbelkilani.clock.enumeration.TimeCounterState
import com.arbelkilani.clock.listener.TimeCounterListener
import ru.mertsalovda.tournament.R
import java.util.concurrent.TimeUnit


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BoardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mTimer: Clock

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
        // Inflate the layout for this fragment
        val time = TimeUnit.SECONDS.toMillis(10)

        val view = inflater.inflate(R.layout.fr_board, container, false)
        mTimer = view.findViewById(R.id.timer)
        mTimer.setOnClickListener {
            when (mTimer.timeCounterState) {
                TimeCounterState.stopped -> mTimer.runTimeCounter(time)
                TimeCounterState.paused -> mTimer.resumeTimeCounter()
                TimeCounterState.running -> mTimer.pauseTimeCounter()
            }
        }

        mTimer.setTimeCounterListener(object : TimeCounterListener {
            override fun onTimeCounterCompleted() {
                Toast.makeText(context, "END", Toast.LENGTH_LONG).show()
            }

            override fun onTimeCounterStateChanged(timeCounterState: TimeCounterState) {
                Toast.makeText(context, timeCounterState.name, Toast.LENGTH_LONG).show()
            }

        })
        return view
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