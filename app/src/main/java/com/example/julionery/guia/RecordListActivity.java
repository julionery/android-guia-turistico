package com.example.julionery.guia;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.julionery.guia.BD.BancoDAO;
import com.example.julionery.guia.Models.Locais;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.List;

public class RecordListActivity extends AppCompatActivity {

    Context context;
    ListView mListView;
    ArrayList<Locais> mList;
    RecordListAdapter mAdapter = null;
    BancoDAO bd;

    ImageView imageViewIcon;
    Button mBtnUpdateRecord;
    private boolean camera = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        context = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Listar de Locais");

        mListView = findViewById(R.id.listView);
        mList = new ArrayList<>();
        mAdapter = new RecordListAdapter(this, R.layout.row, mList);
        mListView.setAdapter(mAdapter);

        bd = new BancoDAO(this);

        ListarTodos();

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                final CharSequence[] items = {"Atualizar", "Deletar"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(RecordListActivity.this);

                dialog.setTitle("Escolha uma ação:");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {

                            ArrayList<Locais> itens = bd.getAllLocais();
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            for (Locais local : itens) {
                                arrID.add(local.getId());
                            }
                            showDialogUpdate(RecordListActivity.this, arrID.get(position));
                        }

                        if (i == 1) {
                            Cursor c = bd.queryData("SELECT id FROM locais");
                            ArrayList<Locais> itens = bd.getAllLocais();
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            for (Locais local : itens) {
                                arrID.add(local.getId());
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });

                dialog.show();
                return true;
            }
        });
    }

    private void ListarTodos() {
        mList.clear();
        ArrayList<Locais> itens = bd.getAllLocais();
        for (Locais local : itens) {
            mList.add(local);
        }

        mAdapter.notifyDataSetChanged();

        if (mList.size() == 0) {
            Toast.makeText(this, "Nenhum Registro Encontrado!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialogDelete(final int idRecord) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(RecordListActivity.this);
        dialogDelete.setTitle("Atenção");
        dialogDelete.setMessage("Voçê tem certeza que desseja deletar?");

        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    bd.deleteLocal(idRecord);
                    Toast.makeText(RecordListActivity.this, "Deletado com sucesso!", Toast.LENGTH_SHORT).show();
                    ListarTodos();
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
            }
        });

        dialogDelete.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogDelete.show();
    }

    private void showDialogUpdate(final Activity activity, int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle("Atualizar");

        Locais localToUpdate = bd.buscarLocalByID(position);

        imageViewIcon = dialog.findViewById(R.id.imageViewRecord);

        final EditText mTxtNomeRecord = findViewById(R.id.edtTituloRecord);
        final EditText mTxtDescricaoRecord = findViewById(R.id.edtDescricaoRecord);
        final EditText mTxtValorRecord = findViewById(R.id.edtValorRecord);
        final EditText mTxtEnderecoRecord = findViewById(R.id.edtLocalizacaoRecord);
        //final EditText mTxtGuiaRecord = findViewById(R.id.edtGuia);
        mBtnUpdateRecord = findViewById(R.id.btnUpdate);

        //mTxtNomeRecord.setText(localToUpdate.getTitulo());
        //mTxtDescricaoRecord.setText(localToUpdate.getDescricao());
        //mTxtValorRecord.setText(localToUpdate.getValor().toString());
        //mTxtEnderecoRecord.setText(localToUpdate.getLocalizacao());

        //Bitmap bitmap = BitmapFactory.decodeByteArray(localToUpdate.getFoto(), 0, localToUpdate.getFoto().length);
        //imageViewIcon.setImageBitmap(bitmap);

        int whidth = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);

        dialog.getWindow().setLayout(whidth, height);
        dialog.show();

        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        RecordListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        /*
        mBtnUpdateRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (mTxtNomeRecord.getText().toString().trim().equals("")) {
                        ShowMessage("Informe o Título");
                        return;
                    }
                    if (mTxtDescricaoRecord.getText().toString().trim().equals("")) {
                        ShowMessage("Informe a Descrição");
                        return;
                    }

                    Locais local = new Locais();
                    local.setTitulo(mTxtNomeRecord.getText().toString().trim());
                    local.setDescricao(mTxtDescricaoRecord.getText().toString().trim());
                    local.setLocalizacao(mTxtEnderecoRecord.getText().toString().trim());
                    if (!mTxtEnderecoRecord.getText().toString().trim().isEmpty()) {
                        local.setValor(Double.parseDouble(mTxtValorRecord.getText().toString()));
                    }
                    bd.updateLocais(local);
                    dialog.dismiss();
                    Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();

                    ListarTodos();

                } catch (Exception ex) {
                    Log.e("error", ex.getMessage());
                }

                ListarTodos();

            }
        });
        */
    }

    private void ShowMessage(String msg) {
        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setTitle("Oooops!");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        android.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("images/*");
                startActivityForResult(galleryIntent, 888);
            } else {
                Toast.makeText(this, "Você não possui permissao de acesso aos arquivos!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 888 && requestCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (requestCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageViewIcon.setImageURI(resultUri);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
