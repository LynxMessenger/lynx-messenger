import android.widget.AbsListView.RecyclerListener
import android.widget.TextView

class MenuAdapter(private val chatlist: List<ChatItem>) :
        RecyclerView.Adapter<ChatAdapter.ChatViewHolder>(){
        class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val avatar: ImageView = itemView.findViewById(R.id.avatarview)
                val username: TextView = itemView.findViewById(R.id.usernameview)
                val message: TextView = itemView.findViewById(R.id.messagetextview)
                val Time: TextView = itemView.findViewById(R.id.timeview)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_chat, parent, false)
                return ChatViewHolder(view)
        }
        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
                val chat = chatList[position]
                holder.avatar.setImageResource(chat.avatarResId)
                holder.username.text = chat.username
                holder.message.text = chat.message
        }
        override fun getItemCount(): Int = chatList.size
}