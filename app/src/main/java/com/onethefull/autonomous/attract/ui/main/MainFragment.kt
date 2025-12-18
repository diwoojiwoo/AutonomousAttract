package com.onethefull.autonomous.attract.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.onethefull.autonomous.attract.R
import com.onethefull.autonomous.attract.robot.DrivingManager

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_start_driving).setOnClickListener {
            DrivingManager.startFigureEightPattern()
        }

        view.findViewById<Button>(R.id.button_stop_driving).setOnClickListener {
            DrivingManager.stopMotion()
        }
    }

    override fun onStop() {
        super.onStop()
        // 화면이 보이지 않게 되면 안전을 위해 주행을 멈춥니다.
        DrivingManager.stopMotion()
    }
}
