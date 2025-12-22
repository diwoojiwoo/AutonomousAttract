package com.onethefull.autonomous.attract.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.onethefull.autonomous.attract.R
import com.onethefull.autonomous.attract.robot.DrivingManager
import com.onethefull.autonomous.attract.robot.InfinityDrivingManager

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_infinity).setOnClickListener {
            InfinityDrivingManager.startInfinityLoop()
        }

        view.findViewById<Button>(R.id.button_once).setOnClickListener {
//            InfinityDrivingManager.startInfinityOnce()
        }

        view.findViewById<Button>(R.id.button_stop_driving).setOnClickListener {
            InfinityDrivingManager.emergencyStop()
        }
    }

    override fun onStop() {
        super.onStop()
        DrivingManager.stop()
    }
}
