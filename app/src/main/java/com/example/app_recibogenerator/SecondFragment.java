package com.example.app_recibogenerator;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_recibogenerator.databinding.ActivityMainBinding;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.app_recibogenerator.databinding.FragmentSecondBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class SecondFragment extends Fragment {

    private void saveBitmapToFile(Bitmap bitmap) {
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "YourDirectory");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, "your_image.png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            // Notify the user that the file is saved and provide the file URI for downloading
            String message = "Image saved. You can download it from: " + Uri.fromFile(file);
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                EditText textView = binding.editTextTextPersonName;

                EditText editText = binding.editTextTextPersonName;
                String valor1_f2 = editText.getText().toString();

                EditText editText2 = binding.editTextTextPersonName2;
                String valor2_f2 = editText2.getText().toString();

                //

                ImageView imageView = binding.imageView;
                Button button = binding.buttonSecond;
                //
                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.recibo);
                Bitmap mutableBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(mutableBitmap);

                //imageView.draw(canvas);

                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(64); //TAMANHO EXATO 59 mas DPS DESCOBRI 64
                //
                Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "seguibl.ttf");
                paint.setTypeface(typeface);

                //Daq pra frente [       canvas.drawText(texto, x, y, paint)      ] está funcionando
                //canvas.drawText(valor1_f2+ ", " + valor2_f2+".", 75, 178, paint);
                //
                //String fulana = "FULANA MARIA DE PERNAMBUCO";
                String mes_Formatado = valor1_f2.toUpperCase(Locale.ROOT);
                //
                String textoOriginal = "RECEBI, DO SR. FULANO, A QUANTIA DE R$ 000,00 (ZERO REAIS), REFERENTE AO PAGAMENTO DO ALUGUEL DO MÊS DE "+mes_Formatado+", DA CASA SITUADA NA RUA X, 000, LOCAL Y, MUNICIPIO-ESTADO. /end";

                // X_inicial = 125
                // X_final_max = 2090

                //

                //
                String[] arrayPalavras = textoOriginal.split(" ");
                int counterPalavras = 0;
                int lenArrayPalavras = arrayPalavras.length;

                String[] arrayLinhas = new String[0]; // Inicia array vazia

                String linhaAtual = "";

                while (counterPalavras<lenArrayPalavras){
                    float numTest = paint.measureText(linhaAtual) + paint.measureText(arrayPalavras[counterPalavras]);
                    String strTest = Float.toString(numTest);
                    //Log.d("Tag", linhaAtual+ " //"+strTest+"- (while)");

                    if (paint.measureText(linhaAtual) + paint.measureText(arrayPalavras[counterPalavras]) <= 2070){

                        linhaAtual += arrayPalavras[counterPalavras]+" ";
                        counterPalavras += 1;
                        Log.d("Tag", linhaAtual+ " //"+strTest+"- (while) IFFFF");

                        if (counterPalavras == lenArrayPalavras-1){
                            String[] arrayLinhas_nova = new String[arrayLinhas.length + 1];
                            System.arraycopy(arrayLinhas, 0, arrayLinhas_nova, 0, arrayLinhas.length);
                            arrayLinhas_nova[arrayLinhas_nova.length - 1] = linhaAtual;

                            arrayLinhas = arrayLinhas_nova;

                            Log.d("Tag", linhaAtual+ " //"+strTest+"- (while) ELSSS");

                            linhaAtual = "";
                            }

                        }
                    else{
                        int spaceCounter = 1;

                        // Alinhando espaçamento
                        while (paint.measureText(linhaAtual) + paint.measureText(" ") < 2060){
                            String[] linhaAtual_array = linhaAtual.split(" ");
                            String[] linhaAtual_array_nova = new String[linhaAtual_array.length + 1];

                            System.arraycopy(linhaAtual_array, 0, linhaAtual_array_nova, 0, spaceCounter);
                            linhaAtual_array_nova[spaceCounter] = " ";
                            System.arraycopy(linhaAtual_array, spaceCounter, linhaAtual_array_nova, spaceCounter+1, linhaAtual_array.length - spaceCounter);

                            linhaAtual = "";

                            for (int i = 0; i< linhaAtual_array_nova.length; i++){
                                linhaAtual += linhaAtual_array_nova[i]+ " ";
                            };

                            if (spaceCounter + 1 < linhaAtual_array.length) { // from +3 to + 1 or 2
                                spaceCounter += 3;
                            }
                            else{
                                spaceCounter = 1;
                            }

                            Log.d("Tag", linhaAtual);


                        }

                        //

                        String[] arrayLinhas_nova = new String[arrayLinhas.length + 1];

                        System.arraycopy(arrayLinhas, 0, arrayLinhas_nova, 0, arrayLinhas.length);
                        arrayLinhas_nova[arrayLinhas_nova.length - 1] = linhaAtual;

                        arrayLinhas = arrayLinhas_nova;

                        //Log.d("Tag", linhaAtual+ " //"+strTest+"- (while) ELSSS");

                        linhaAtual = "";



                    };




                };

                //
                int Y_adder = 0;
                int i_counter = 0;
                for (String arrayLinha : arrayLinhas) {
                    Log.d("Tag", arrayLinha);
                    canvas.drawText(arrayLinhas[i_counter], 106, 325+Y_adder, paint);
                    Y_adder += 80;
                    i_counter += 1;
                }

                String dataFormatada = "";
                String[] componentesData = valor2_f2.split("/");
                //

                String diaDoMes = componentesData[0];
                String numAno = componentesData[2];

                String[] nomesMeses = {
                        "JANEIRO", "FEVEREIRO", "MARÇO", "ABRIL", "MAIO", "JUNHO", "JULHO",
                        "AGOSTO", "SETEMBRO", "OUTUBRO", "NOVEMBRO", "DEZEMBRO"
                };
                int numMes = Integer.parseInt(componentesData[1]);
                String nomeMes = nomesMeses[numMes-1];
                dataFormatada = diaDoMes + " DE "+ nomeMes + " DE "+numAno;

                //
                canvas.drawText("VITÓRIA-PE, "+dataFormatada+".", 106, 830, paint);
                //

                //float tamanhoTeste = paint.measureText(textoOriginal);
                //String tamanhoTeste2 = Float.toString(tamanhoTeste);
                //canvas.drawText(tamanhoTeste2, 2090, 305, paint);

                // -------------------------------------------------------

                // FAZER CONSEGUIR BAIXAR AINDA

                saveBitmapToFile(mutableBitmap);

                //============
                imageView.setImageBitmap(mutableBitmap);

                Snackbar.make(view, "Baixando imagem... ("+valor1_f2+", "+valor2_f2+")", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //NavHostFragment.findNavController(SecondFragment.this)
                //        .navigate(R.id.action_SecondFragment_to_FirstFragment);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}