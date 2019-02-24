package app.com.bookdb.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import app.com.bookdb.R;
import app.com.bookdb.model.Book;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder>{

    public List<Book> books;
    private Context context;

    public BookAdapter(Context context){
        this.context = context;
    }

    class BookHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_parent_layout)RelativeLayout parentLayout;
        @BindView(R.id.rv_order_number_book)AppCompatTextView orderNumberBook;
        @BindView(R.id.rv_title_book)AppCompatTextView titleBook;


        public BookHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public interface OnBookAdapterListener{
        void onItemClick(Book book);
    }

    private OnBookAdapterListener mListener;

    public void setListener(OnBookAdapterListener listener){
        this.mListener = listener;
    }

    public void setData(List<Book> books){
        this.books = books;
    }

    @Override
    public int getItemCount() {
        return (books == null) ? 0 : books.size();
    }

    @Override
    public void onBindViewHolder(final BookHolder bookHolder, int pos) {
        final Book book = books.get(pos);
        bookHolder.orderNumberBook.setText(""+(pos+1));
        bookHolder.titleBook.setText(book.getName());
        bookHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(books.get(bookHolder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public BookHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new BookHolder(LayoutInflater.from(context).inflate(R.layout.rv_book_item,viewGroup,false));
    }
}
