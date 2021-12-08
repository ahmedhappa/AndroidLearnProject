package com.example.androidlearnproject.animation.transitions_materialmotion.container_transform.example2_act_fra_list_fab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlearnproject.databinding.ItemCtFirstScreenBinding

class AdapterFirstScreen(private val dataList: List<Pair<String, String>>, private val clickItem: (Int, CardView) -> Unit) :
    RecyclerView.Adapter<AdapterFirstScreen.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCtFirstScreenBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], clickItem)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(private val itemViewBinding: ItemCtFirstScreenBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bind(data: Pair<String, String>, clickItem: (Int, CardView) -> Unit) {
            //add transition name to a view to indicate which element will be used during transition
            //You need to use the id or unique value of each item to ensure each transitionName is unique
            /* Setting transition name to this CardView and transition name to the other CardView In the second screen
                 will help inform the Android Transition system of the two shared element views that are involved in this transition.
             */
            itemViewBinding.cvContainer.transitionName = "firstScreenCardTransition$layoutPosition"

            itemViewBinding.tvName.text = data.first
            itemViewBinding.tvDescription.text = data.second

            itemViewBinding.root.setOnClickListener {
                clickItem(layoutPosition, itemViewBinding.cvContainer)
            }

        }
    }


}