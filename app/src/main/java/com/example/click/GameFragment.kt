package com.example.click

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.click.databinding.FragmentGameBinding


class GameFragment : Fragment() {
    private lateinit var binding:FragmentGameBinding

    private lateinit var viewModel: GameViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_game,container,false)

        viewModel=ViewModelProvider(this).get(GameViewModel::class.java)

        viewModel.currenttime.observe(viewLifecycleOwner, Observer { time->binding.gameTvTimer.text=DateUtils.formatElapsedTime(time) })
        viewModel.currentbutton.observe(viewLifecycleOwner, Observer { button->moveButton(button) })
        viewModel.score.observe(viewLifecycleOwner, Observer { score->binding.textView2.text=score.toString() })
        viewModel.scorecolor.observe(viewLifecycleOwner, Observer { color->changeColor(color) })
        viewModel.gamefinished.observe(viewLifecycleOwner, Observer { isfinished ->if(isfinished)gameover() })

        viewModel.eventBuzz.observe(viewLifecycleOwner, Observer { buzzType ->
            if (buzzType != GameViewModel.BuzzType.NO_BUZZ) {
                buzz(buzzType.pattern)
                viewModel.onBuzzComplete()
            }
        })

        binding.gameBtBut1.setOnClickListener { viewModel.gainPoint() }
        binding.gameBtBut2.setOnClickListener { viewModel.gainPoint() }
        binding.gameBtBut3.setOnClickListener { viewModel.gainPoint() }
        binding.gameBtBut4.setOnClickListener { viewModel.gainPoint() }
        binding.layoutId.setOnClickListener { viewModel.losePoint() }



        return binding.root
    }

    private fun moveButton(button:Int){
        binding.gameBtBut1.visibility=View.INVISIBLE
        binding.gameBtBut2.visibility=View.INVISIBLE
        binding.gameBtBut3.visibility=View.INVISIBLE
        binding.gameBtBut4.visibility=View.INVISIBLE

        when(button){
            1->binding.gameBtBut1.visibility=View.VISIBLE
            2->binding.gameBtBut2.visibility=View.VISIBLE
            3->binding.gameBtBut3.visibility=View.VISIBLE
            else->binding.gameBtBut4.visibility=View.VISIBLE
        }



    }

    private fun changeColor(color:String){
        if(color=="coral"){
            binding.textView2.setTextColor(ContextCompat.getColor(requireContext().applicationContext, R.color.coral))
        }
        else{
            binding.textView2.setTextColor(ContextCompat.getColor(requireContext().applicationContext,R.color.redbg))
        }
    }
    private fun gameover(){
        findNavController().navigate(GameFragmentDirections.actionGameFragmentToScoreFragment(viewModel.score.value!!))

    }
    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }
}