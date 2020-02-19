package com.naugo.naugo.ui.aboutus;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.naugo.naugo.R;

public class AboutUsFragment extends Fragment {

    private AboutUsViewModel aboutUsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        aboutUsViewModel =
                ViewModelProviders.of(this).get(AboutUsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);

        TextView email = (TextView) root.findViewById(R.id.tvemail);
        TextView whatsapp = (TextView) root.findViewById(R.id.tvwa);
        TextView sky = (TextView) root.findViewById(R.id.tvsky);
        TextView mes = (TextView) root.findViewById(R.id.tvmes);


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:"+"cs.en@naugo.app"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "email_subject");
                intent.putExtra(Intent.EXTRA_TEXT, "email_body");
                startActivity(intent);
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "+65 8668 5707";
                String url = "https://api.whatsapp.com/send?phone="+number;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        sky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = "+65 8668 5707";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("skype:" + user_name));
                startActivity(i);
            }
        });

        mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String peopleId = "100045325517518";
                Uri uri = Uri.parse("fb-messenger://user/");
                uri = ContentUris.withAppendedId(uri, Long.parseLong(peopleId));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        final TextView textView = root.findViewById(R.id.text_share);
        aboutUsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


}