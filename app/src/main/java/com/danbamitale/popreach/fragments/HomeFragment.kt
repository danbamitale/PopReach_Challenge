package com.danbamitale.popreach.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.view.doOnLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.danbamitale.popreach.R
import com.danbamitale.popreach.databinding.FragmentHomeBinding
import com.danbamitale.popreach.models.ShapeFactory
import com.danbamitale.popreach.utils.BindingFragment
import com.danbamitale.popreach.models.ShapeOperation
import com.danbamitale.popreach.viewmodels.HomeFragmentViewModel
import com.danbamitale.popreach.views.TouchEvent
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class HomeFragment : BindingFragment<FragmentHomeBinding>() {

    private val viewModel: HomeFragmentViewModel by activityViewModels()

    override fun bindView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, parent, attachToParent)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.doOnLayout {
            val padding =  binding.canvasView.paddingLeft
            viewModel.setViewDimension(binding.canvasView.measuredWidth - padding, binding.canvasView.measuredHeight - padding)
        }

        viewModel.resetState() // remove all state from the viewmodel so the UI does not show stale states when screen is resumed
        if (viewModel.shapes.isNotEmpty()) {// resuming View
            //refresh view
            binding.canvasView.clear()
            binding.canvasView.drawAll(viewModel.shapes)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.onError.collect {
                        it?.let {
                            Snackbar.make(binding.snackbarPosView, it.localizedMessage, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }

                launch {
                    viewModel.operation.collect {
                        it?.let {
                            binding.undoBtn.isEnabled = true
                            when (it) {
                                is ShapeOperation.Create -> binding.canvasView.draw(it.shape)
                                is ShapeOperation.Delete -> binding.canvasView.remove(it.shape)
                                is ShapeOperation.Transform -> {
                                    binding.canvasView.remove(it.old)
                                    binding.canvasView.draw(it.now)
                                }
                                is ShapeOperation.DeleteAll -> binding.canvasView.remove(*it.shapes.toTypedArray())
                            }
                        } ?: kotlin.run {
                            binding.undoBtn.isEnabled = false
                        }
                    }
                }

                launch {
                    binding.canvasView.onTouch.collect {
                        when (it) {
                            is TouchEvent.Press -> viewModel.transform(it.point)
                            is TouchEvent.LongPress -> viewModel.delete(it.point)
                        }
                    }
                }
            }
        }


        binding.squareBtn.setOnClickListener {
            viewModel.draw(ShapeFactory.Type.Square)
        }

        binding.circleBtn.setOnClickListener {
            viewModel.draw(ShapeFactory.Type.Circle)
        }

        binding.triangleBtn.setOnClickListener {
            viewModel.draw(ShapeFactory.Type.Triangle)
        }

        binding.undoBtn.setOnClickListener {
            viewModel.operation.value?.let {
                when (it) {
                    is ShapeOperation.Create -> binding.canvasView.remove(it.shape)
                    is ShapeOperation.Delete -> binding.canvasView.draw(it.shape)
                    is ShapeOperation.Transform -> {
                        binding.canvasView.remove(it.now)
                        binding.canvasView.draw(it.old)
                    }
                }
                viewModel.undo()
            }
        }

        binding.statsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

}