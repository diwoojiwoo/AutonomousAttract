package com.onethefull.autonomous.attract.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.onethefull.autonomous.attract.R
import com.onethefull.autonomous.attract.databinding.FragmentMainBinding
import com.onethefull.autonomous.attract.robot.DrivingManager
import com.onethefull.autonomous.attract.robot.InfinityDrivingManager
import com.onethefull.autonomous.attract.utils.animation.BackgroundUiActionDasom
import com.onethefull.autonomous.attract.utils.logger.DWLog
import kotlinx.coroutines.delay

class MainFragment : Fragment(), MainContract.View {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: MainContract.Presenter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        presenter = MainPresenter(requireActivity(), this, BackgroundUiActionDasom(requireActivity(), binding.root))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.start()

        view.findViewById<Button>(R.id.button_infinity).setOnClickListener {
            presenter.speakMent()
        }

        view.findViewById<Button>(R.id.button_stop_driving).setOnClickListener {
            InfinityDrivingManager.emergencyStop()
        }
    }

    override fun onResume() {
        super.onResume()
        DWLog.d("MainFragment : onResume (isAdded:$isAdded)")
        presenter.init()
    }

    override fun showToast(msg: String) {}

    override fun onStop() {
        super.onStop()
        DrivingManager.stop()
    }

    override fun onPause() {
        super.onPause()
        DWLog.d("MainFragment : onPause")
        presenter.disconnect()
    }
}
