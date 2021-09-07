package co.tiagoaguiar.atway.example.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import co.tiagoaguiar.atway.example.databinding.ActivityExampleListBinding
import co.tiagoaguiar.atway.example.databinding.ItemPostBinding
import co.tiagoaguiar.atway.ui.ATActivity
import co.tiagoaguiar.atway.ui.adapter.ATAdapter
import co.tiagoaguiar.atway.ui.adapter.ATViewHolder

class ExampleListActivity : ATActivity<ActivityExampleListBinding>(ActivityExampleListBinding::inflate) {

  private val postAdapter = ATAdapter(
    viewHolder = { PostView(it) }
  ) { post ->
    println(post)
  }

  override fun setupViews() {
    binding.rv.layoutManager = LinearLayoutManager(this)
    binding.rv.adapter       = postAdapter

    postAdapter.items = arrayListOf(
      Post("Hello", "World"),
      Post("I am", "a post"),
    )
  }

}

data class Post(
  val firstName: String,
  val lastName: String,
)

class PostView(root: ViewGroup) : ATViewHolder<Post, ItemPostBinding>(ItemPostBinding::inflate, root) {

  override fun bind(item: Post) {
    with(binding) {
      txtTitle.text    = item.firstName
      txtSubtitle.text = item.lastName
    }
  }

}