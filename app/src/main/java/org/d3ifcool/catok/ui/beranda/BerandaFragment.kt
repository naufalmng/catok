package org.d3ifcool.catok.ui.beranda

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import org.d3ifcool.catok.R
import org.d3ifcool.catok.databinding.FragmentBerandaBinding
import org.d3ifcool.catok.ui.main.SharedViewModel
import org.d3ifcool.catok.ui.pengaturan.PengaturanViewModel
import org.d3ifcool.catok.utils.enableOnClickAnimation
import org.koin.androidx.viewmodel.ext.android.viewModel

class BerandaFragment : Fragment() {

    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }
    private val pengaturanViewModel: PengaturanViewModel by viewModel()

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
        setupObservers()
    }

    private fun setupObservers() {
        pengaturanViewModel.getDataProfil.observe(viewLifecycleOwner){
            Log.d("PengaturanFragment", "setupObservers: $it")
            if(it!=null){
                binding.circleImage.load(sharedViewModel.getFotoToko.value?:it.gambar)
                binding.namaToko.text = sharedViewModel.getNamaToko.value?:it.namaToko
            }
            else{
                binding.circleImage.setImageBitmap(ContextCompat.getDrawable(requireContext(),R.drawable.ic_catok)!!.toBitmap())
                binding.namaToko.text = getString(R.string.nama_toko)
            }
        }
    }

    private fun setupListeners() {
        with(binding){
            dataProduk.setOnClickListener{
               findNavController().navigate(R.id.action_berandaFragment_to_dataProdukFragment)
            }
            transaksiProduk.setOnClickListener{
                findNavController().navigate(R.id.action_berandaFragment_to_transaksiFragment)

            }
        }
    }

    private fun enableBtnAnimation() {
        with(binding){
            dataProduk.enableOnClickAnimation()
            transaksiProduk.enableOnClickAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}