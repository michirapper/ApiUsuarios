package com.example.ejemplosebas.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.ejemplosebas.R
import com.example.ejemplosebas.adapters.UsersAdapter
import com.example.ejemplosebas.databinding.FragmentListaBinding
import com.example.ejemplosebas.model.UserViewModel
import com.example.ejemplosebas.pojo.User
import kotlinx.datetime.toLocalDateTime
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException

class ListaFragment : Fragment() {
    private lateinit var binding: FragmentListaBinding
    private var lista= MutableLiveData<List<User>>()
    lateinit var recyclerViewLista: RecyclerView
    lateinit var adapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentListaBinding.inflate(inflater, container, false)
        recyclerViewLista = binding.recyclerviewlista

        jsonParser()

//        lista.observe(viewLifecycleOwner, Observer {
//            //var cuantos = lista.value!!.size
//            var birthdate = lista.value!!.get(0).birthdate.toString()
//
//        })

        return binding.root

    }
    private fun jsonParser(){
        val url = "https://hello-world.innocv.com/api/User"

        val requestQueue = Volley.newRequestQueue(context)

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    var listaUsuarios = ArrayList<User>()
                    for (contador in 0..response.length() - 1) {
                        val readData = response.getJSONObject(contador)
                        val id = readData.getInt("id")
                        val name = readData.getString("name")
                        val birthdate = readData.getString("birthdate").toLocalDateTime()
                        listaUsuarios.add(User(id, name, birthdate))
                    }
                    lista.value = listaUsuarios
                    listaUsuarios()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(context, getVolleyError(error), Toast.LENGTH_LONG).show()
            })

        requestQueue.add(request)
    }
    private fun listaUsuarios(){
        var viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        adapter = UsersAdapter(lista.value!!, viewModel)
        recyclerViewLista.adapter = adapter
        recyclerViewLista.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

    fun getVolleyError(error: VolleyError): String {
        var errorMsg = ""
        if (error is NoConnectionError) {
            val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetwork: NetworkInfo? = null
            activeNetwork = cm.activeNetworkInfo
            errorMsg = if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
                "Server is not connected to the internet. Please try again"
            } else {
                "Your device is not connected to internet.please try again with active internet connection"
            }
        } else if (error is NetworkError || error.cause is ConnectException) {
            errorMsg = "Your device is not connected to internet.please try again with active internet connection"
        } else if (error.cause is MalformedURLException) {
            errorMsg = "That was a bad request please try again…"
        } else if (error is ParseError || error.cause is IllegalStateException || error.cause is JSONException || error.cause is XmlPullParserException) {
            errorMsg = "There was an error parsing data…"
        } else if (error.cause is OutOfMemoryError) {
            errorMsg = "Device out of memory"
        } else if (error is AuthFailureError) {
            errorMsg = "Failed to authenticate user at the server, please contact support"
        } else if (error is ServerError || error.cause is ServerError) {
            errorMsg = "Internal server error occurred please try again...."
        } else if (error is TimeoutError || error.cause is SocketTimeoutException || error.cause is ConnectTimeoutException || error.cause is SocketException || (error.cause!!.message != null && error.cause!!.message!!.contains(
                "Your connection has timed out, please try again"
            ))
        ) {
            errorMsg = "Your connection has timed out, please try again"
        } else {
            errorMsg = "An unknown error occurred during the operation, please try again"
        }
        return errorMsg
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        var menuItemSearch = menu.findItem(R.id.app_bar_search)
        menuItemSearch.setVisible(true)

        var searchView = menuItemSearch.actionView as SearchView
        searchView.queryHint = "Buscar Usuarios"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }

        })

        super.onCreateOptionsMenu(menu, inflater)

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListaFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}