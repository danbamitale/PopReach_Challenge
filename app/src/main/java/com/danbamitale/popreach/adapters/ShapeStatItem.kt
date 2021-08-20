package com.danbamitale.popreach.adapters

import android.view.View
import com.danbamitale.popreach.R
import com.danbamitale.popreach.adapters.baseadapter.BaseItem
import com.danbamitale.popreach.databinding.ShapeStatsListItemBinding
import com.danbamitale.popreach.models.ShapeStat

data class ShapeStatItem(val stat: ShapeStat): BaseItem<ShapeStatsListItemBinding> {

    override val layoutId: Int
        get() = R.layout.shape_stats_list_item

    override val uniqueId: Any
        get() = stat.type

    override fun initializeViewBinding(view: View): ShapeStatsListItemBinding {
        return ShapeStatsListItemBinding.bind(view)
    }

    override fun bind(
        binding: ShapeStatsListItemBinding,
        itemClickCallback: ((BaseItem<ShapeStatsListItemBinding>) -> Unit)?
    ) {
        binding.shapeTypeTitle.text = stat.type.name
        binding.shapeCountText.text = stat.count.toString()

        binding.deleteBtn.setOnClickListener {
            itemClickCallback?.let {
                it.invoke(this)
                binding.deleteBtn.isEnabled = false
                binding.shapeCountText.text = "0"
            }
        }

        binding.deleteBtn.isEnabled = (stat.count > 0)
    }

}