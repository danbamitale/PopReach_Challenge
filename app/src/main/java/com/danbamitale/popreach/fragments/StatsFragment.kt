package com.danbamitale.popreach.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.danbamitale.popreach.adapters.ShapeStatItem
import com.danbamitale.popreach.adapters.baseadapter.BaseListAdapter
import com.danbamitale.popreach.databinding.FragmentStatsBinding
import com.danbamitale.popreach.models.*
import com.danbamitale.popreach.utils.BindingFragment
import com.danbamitale.popreach.viewmodels.HomeFragmentViewModel


class StatsFragment : BindingFragment<FragmentStatsBinding>() {

    private val viewModel: HomeFragmentViewModel by activityViewModels()
    private lateinit var shapeAdapter: BaseListAdapter

    override fun bindView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): FragmentStatsBinding {
        val binding = FragmentStatsBinding.inflate(layoutInflater, parent, attachToParent)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shapeAdapter =  BaseListAdapter {
            val statItem = it as ShapeStatItem

            val classType =  when (it.stat.type) {
                ShapeFactory.Type.Square -> Square::class.java
                ShapeFactory.Type.Circle -> Circle::class.java
                ShapeFactory.Type.Triangle -> Triangle::class.java
            }

            viewModel.deleteAll(classType)
        }
        binding.recyclerView.adapter = shapeAdapter
        createShapeStatItems(viewModel.shapes)
    }

    private fun createShapeStatItems(list: List<Shape>) {
        var circleCount = 0; var squareCount = 0; var triangleCount = 0
        list.forEach {
            when (it) {
                is Square -> squareCount++
                is Circle -> circleCount++
                is Triangle -> triangleCount++
            }
        }

        shapeAdapter.submitList(
            listOf(
                ShapeStatItem(ShapeStat(ShapeFactory.Type.Circle, circleCount)),
                ShapeStatItem(ShapeStat(ShapeFactory.Type.Square, squareCount)),
                ShapeStatItem(ShapeStat(ShapeFactory.Type.Triangle, triangleCount)),
            )
        )
    }

}