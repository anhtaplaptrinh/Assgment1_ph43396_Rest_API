package com.example.assgment_ph43396_rest_api_and;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class Home extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterSanpham adapter;
    List<SanphamModel> list;
    APIServer apiService;
    EditText edten, edgia, edsoluong,search;
    ImageView anh;
    Uri selectedImage;
    SanphamModel sanphamModel;
    File file;
    MultipartBody.Part multipartBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recyclerViewSinhVien);
        search = findViewById(R.id.search);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIServer.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIServer.class);

        loadData();

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                them(Home.this, 0, sanphamModel);
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString().trim();

                searchSanphamModel(keyword);
            }
        });
        findViewById(R.id.giam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<SanphamModel>> call = apiService.getGiam();
                call.enqueue(new Callback<List<SanphamModel>>() {
                    @Override
                    public void onResponse(Call<List<SanphamModel>> call, Response<List<SanphamModel>> response) {
                        if (response.isSuccessful()) {
                            list = response.body();

                            adapter = new AdapterSanpham(list,  getApplicationContext(), Home.this);

                            recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SanphamModel>> call, Throwable t) {
                        Log.e("Main", t.getMessage());
                    }
                });
            }
        });
        findViewById(R.id.tang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<SanphamModel>> call = apiService.getTang();
                call.enqueue(new Callback<List<SanphamModel>>() {
                    @Override
                    public void onResponse(Call<List<SanphamModel>> call, Response<List<SanphamModel>> response) {
                        if (response.isSuccessful()) {
                            list = response.body();

                            adapter = new AdapterSanpham(list,  getApplicationContext(), Home.this);

                            recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SanphamModel>> call, Throwable t) {
                        Log.e("Main", t.getMessage());
                    }
                });
                Toast.makeText(Home.this, "Linh dep trai", Toast.LENGTH_SHORT).show();
            }
        });

    }


    void loadData(){
        Call<List<SanphamModel>> call = apiService.getSanphams();

        call.enqueue(new Callback<List<SanphamModel>>() {
            @Override
            public void onResponse(Call<List<SanphamModel>> call, Response<List<SanphamModel>> response) {
                if (response.isSuccessful()) {
                    list = response.body();

                    adapter = new AdapterSanpham(list,  getApplicationContext(), Home.this);


                    recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<SanphamModel>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImage = data.getData();
            anh.setImageURI(selectedImage);
        }
    }

    public void them(Context context, int type, SanphamModel sanphamModel) {
        Dialog dialog = new Dialog(Home.this);
        dialog.setContentView(R.layout.dialog_save_upsate);

        edten = dialog.findViewById(R.id.edtten);
        edgia = dialog.findViewById(R.id.edtgia);
        edsoluong = dialog.findViewById(R.id.edtsoluong);
        anh = dialog.findViewById(R.id.imgImage);

        anh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        if (type != 0){
            edten.setText(sanphamModel.getTen());
            edgia.setText(sanphamModel.getGia()+"");
            edsoluong = dialog.findViewById(R.id.edtsoluong);
            Glide.with(context)
                    .load(sanphamModel.getAnh())
                    .thumbnail(Glide.with(context).load(R.drawable.loading))
                    .into(anh);
            Log.d(TAG, "them: " + sanphamModel.getAnh());
        }
        dialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, RequestBody> mapRequestBody = new HashMap<>();
                String _ten = edten.getText().toString();
                String giastr = edgia.getText().toString();
                String _soluong = edsoluong.getText().toString();

                if (file != null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("anh", file.getName(), requestFile);
                } else {
                    multipartBody = null;
                }

                if (_ten.length() == 0 || giastr.length() == 0 || _soluong.length() == 0){
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    Double _gia = Double.parseDouble(giastr);

                    mapRequestBody.put("ten", getRequestBody(_ten));
                    mapRequestBody.put("gia", getRequestBody(String.valueOf(_gia)));
                    mapRequestBody.put("soluong", getRequestBody(_soluong));

                    if (_gia > 0){
                        if (type == 0){
                            Call<SanphamModel> call = apiService.addSanpham(mapRequestBody, multipartBody);
                            call.enqueue(new Callback<SanphamModel>() {
                                @Override
                                public void onResponse(Call<SanphamModel> call, Response<SanphamModel> response) {
                                    if (response.isSuccessful()) {
                                        loadData();
                                        Toast.makeText(Home.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(Home.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<SanphamModel> call, Throwable t) {
                                    Log.e("Home", "Call failed: " + t.toString());
                                    Toast.makeText(Home.this, "Đã xảy ra lỗi khi thêm dữ liệu", Toast.LENGTH_SHORT).show();
                                }

                            });
                        }else{
                            if (multipartBody != null){
                                Call<SanphamModel> call = apiService.updateSanpham(mapRequestBody, sanphamModel.get_id() , multipartBody);
                                call.enqueue(new Callback<SanphamModel>() {
                                    @Override
                                    public void onResponse(Call<SanphamModel> call, Response<SanphamModel> response) {
                                        if (response.isSuccessful()) {
                                            loadData();
                                            Toast.makeText(Home.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(Home.this, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<SanphamModel> call, Throwable t) {
                                        Log.e("Home", "Call failed: " + t.toString());
                                        Toast.makeText(Home.this, "Đã xảy ra lỗi khi sửa dữ liệu", Toast.LENGTH_SHORT).show();
                                    }

                                });
                            }else{
                                Call<SanphamModel> call = apiService.updateNoImage(mapRequestBody, sanphamModel.get_id());
                                call.enqueue(new Callback<SanphamModel>() {
                                    @Override
                                    public void onResponse(Call<SanphamModel> call, Response<SanphamModel> response) {
                                        if (response.isSuccessful()) {
                                            loadData();
                                            Toast.makeText(Home.this, "Sửa thành công no image", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(Home.this, "Sửa thất bại no image", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<SanphamModel> call, Throwable t) {
                                        Log.e("Home", "Call failed: " + t.toString());
                                        Toast.makeText(Home.this, "Đã xảy ra lỗi khi sửa dữ liệu", Toast.LENGTH_SHORT).show();
                                    }

                                });
                            }
                        }
                    }else{
                        Toast.makeText(context, "Giá phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    }

                }catch (NumberFormatException e){
                    Toast.makeText(context, "Gía phải là số", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public  void xoa(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle("Delete");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Void> call = apiService.deleteSanpham(id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            loadData();
                            Toast.makeText(Home.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Home.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Home", "Call failed: " + t.toString());
                        Toast.makeText(Home.this, "Đã xảy ra lỗi khi xóa dữ liệu", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        builder.show();

    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getImage.launch(intent);

    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        Uri imageUri = data.getData();

                        Log.d("RegisterActivity", imageUri.toString());

                        file = createFileFormUri(imageUri, "anh");

                        Glide.with(anh)
                                .load(imageUri)
                                .centerCrop()
                                .circleCrop()
                                .into(anh);
                    }
                }
            });

    private File createFileFormUri(Uri path, String name) {
        File _file = new File(Home.this.getCacheDir(), name + ".png");
        try {
            InputStream in = Home.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

    private void searchSanphamModel(String keyword) {
        Call<List<SanphamModel>> call = apiService.searchCay(keyword);
        call.enqueue(new Callback<List<SanphamModel>>() {
            @Override
            public void onResponse(Call<List<SanphamModel>> call, Response<List<SanphamModel>> response) {
                if (response.isSuccessful()) {
                    list = response.body();

                    adapter = new AdapterSanpham(list,  getApplicationContext(), Home.this);

                    recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<SanphamModel>> call, Throwable t) {
                Log.e("Search", "Search failed: " + t.toString());
                Toast.makeText(Home.this, "Đã xảy ra lỗi kh tìm kiếm", Toast.LENGTH_SHORT).show();
            }
        });
    }

}