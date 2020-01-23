package com.example.julionery.guia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.julionery.guia.Models.Locais;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecordListAdapter extends BaseAdapter {


    private Context context;
    private int layout;
    private ArrayList<Locais> recordList;

    public RecordListAdapter(Context context, int layout, ArrayList<Locais> recordList){
        this.context = context;
        this.layout = layout;
        this.recordList = recordList;
    }


    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) {
        return  recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtTitulo, txtDescricao, txtEndereco, txtValor, txtGuia;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtTitulo = row.findViewById(R.id.txtTitulo);
            holder.txtDescricao = row.findViewById(R.id.txtDescricao);
            holder.txtEndereco = row.findViewById(R.id.txtEndereco);
            holder.txtValor = row.findViewById(R.id.txtValor);
            //holder.txtGuia = row.findViewById(R.id.txtGuia);
            holder.imageView = row.findViewById(R.id.imgIcon);

            row.setTag(holder);
        }else {
            holder = (ViewHolder)row.getTag();
        }

        Locais local = recordList.get(i);

        holder.txtTitulo.setText(local.getTitulo());
        holder.txtDescricao.setText(local.getDescricao());
        holder.txtEndereco.setText(local.getLocalizacao());
        holder.txtValor.setText(local.getValor().toString());
        //holder.txtGuia.setText(local.getGuia());

        byte[] recordImage = local.getFoto();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
