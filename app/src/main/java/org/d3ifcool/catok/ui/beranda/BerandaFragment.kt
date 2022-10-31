package org.d3ifcool.catok.ui.beranda

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import org.d3ifcool.catok.R
import org.d3ifcool.catok.databinding.FragmentBerandaBinding
import org.d3ifcool.catok.utils.enableOnClickAnimation
import org.d3ifcool.catok.utils.onTouch

//import org.d3ifcool.catok.utils.enableOnClickAnimation

class BerandaFragment : Fragment() {

    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        enableBtnAnimation()
        setupListeners()
    }

    private fun setupListeners() {
        with(binding){
            dataProduk.setOnClickListener{
               findNavController().navigate(R.id.action_berandaFragment_to_dataProdukFragment)
            }
            pembelianProduk.setOnClickListener{
                Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()
            }
            historiProduk.setOnClickListener{
                Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()
            }
            penjualanProduk.setOnClickListener{
                Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enableBtnAnimation() {
        with(binding){
            dataProduk.enableOnClickAnimation()
            pembelianProduk.enableOnClickAnimation()
            penjualanProduk.enableOnClickAnimation()
            historiProduk.enableOnClickAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}