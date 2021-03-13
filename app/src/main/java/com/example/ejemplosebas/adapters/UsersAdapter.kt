package com.example.ejemplosebas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ejemplosebas.R
import com.example.ejemplosebas.fragments.ListaFragmentDirections
import com.example.ejemplosebas.model.UserViewModel
import com.example.ejemplosebas.pojo.User

class UsersAdapter(var users: List<User>, var userViewModel: UserViewModel): RecyclerView.Adapter<UsersAdapter.ViewHolder>(), Filterable {

    var FilterList = ArrayList<User>()

    init{
        FilterList = users as ArrayList<User>
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(FilterList[position])
        holder.itemView.setOnClickListener {

            userViewModel.setUserSeleccionada(FilterList[position])

            val action = ListaFragmentDirections.actionListaFragmentToDetallesFragment()
            it.findNavController().navigate(action)

        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return FilterList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(ciudad: User) {
            val textViewNombre = itemView.findViewById<TextView>(R.id.textViewTitulo)
            textViewNombre.text = ciudad.name
        }
    }

    override fun getFilter(): Filter {
       return object :Filter(){
           override fun performFiltering(constraint: CharSequence?): FilterResults {
               val charSequence = constraint.toString()
               if (charSequence.isEmpty()){
                   FilterList = users as ArrayList<User>
               }
               else{
                   val resultList = ArrayList<User>()
                   for (row in users){
                       if (row.name?.toLowerCase()?.contains(charSequence.toLowerCase())!!){
                           resultList.add(row)
                       }
                   }
                   FilterList = resultList
               }
               var filterResult = FilterResults()
               filterResult.values = FilterList
               return filterResult
           }

           override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
               FilterList = results?.values as ArrayList<User>
               notifyDataSetChanged()
           }
       }
    }
}