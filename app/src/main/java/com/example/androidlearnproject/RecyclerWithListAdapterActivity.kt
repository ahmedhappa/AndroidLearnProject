package com.example.androidlearnproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlearnproject.databinding.ActivityRecyclerWithListAdapterBinding
import com.example.androidlearnproject.databinding.ItemRecyclerListAdapterBinding

class RecyclerWithListAdapterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerWithListAdapterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerWithListAdapterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var listOfNames = mutableListOf(
            User(1, "Ahmed"),
            User(2, "Ali")
        )

        val adapter = MyListAdapter()
        adapter.submitList(listOfNames)
        binding.rvListAdapter.layoutManager = LinearLayoutManager(this)
        binding.rvListAdapter.adapter = adapter

        /* submitList function requires you to pass another list so in order to change
             the ui you need to pass a new list object or passing the same list and you need to call notifyDataSetChange() */

        binding.fabAddItem.setOnClickListener {
            //here you need to call notify because you passed the same list object
            listOfNames.add(User(3, "Sayed"))
            adapter.notifyItemInserted(listOfNames.size - 1)
        }

        binding.fabDeleteItem.setOnClickListener {
            //here you need to call notify because you passed the same list object
            listOfNames.removeAt(0)
            adapter.notifyItemRemoved(0)
        }

        binding.fabRefreshItem.setOnClickListener {
            //here you don't need to call notify and call submit list only because you passed a new list object
            listOfNames = mutableListOf(
                User(1, "Ahmed"),
                User(2, "Ali")
            )
            adapter.submitList(listOfNames)
        }

    }

    private class MyListAdapter : ListAdapter<User, MyListAdapter.MyViewHolder>(MyDiffUtils()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(
                ItemRecyclerListAdapterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            //get item is a method in the ListAdapter class that get the list of items
            holder.bind(getItem(position))
        }


        private class MyViewHolder(itemRecyclerListAdapterBinding: ItemRecyclerListAdapterBinding) :
            RecyclerView.ViewHolder(itemRecyclerListAdapterBinding.root) {
            val nameTextView = itemRecyclerListAdapterBinding.tvItemName
            fun bind(user: User) {
                nameTextView.text = user.name
            }
        }

        private class MyDiffUtils : DiffUtil.ItemCallback<User>() {
            //checks if two items are actually the same item
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                //here try to compare unique value
                return oldItem.id == newItem.id
            }

            //checks if two items have the same data.
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

        }

    }

    private data class User(val id: Short, val name: String)

}