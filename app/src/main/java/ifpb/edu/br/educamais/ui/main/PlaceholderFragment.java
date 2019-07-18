package ifpb.edu.br.educamais.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import ifpb.edu.br.educamais.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_organizador, container, false);

        final TextView textView = root.findViewById(R.id.section_label);
        final ImageView imageViewFoto1 = root.findViewById(R.id.imageViewFoto1);
        final ImageView imageViewFoto2 = root.findViewById(R.id.imageViewFoto2);
        final ImageView imageViewFoto3 =  root.findViewById(R.id.imageViewFoto3);

        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);

                //Mostra quando o tab for de mensagens
                if(s.contains("Imagens")){
                    imageViewFoto1.setImageResource(R.drawable.paulo);
                    imageViewFoto2.setImageResource(R.drawable.juliana);
                    imageViewFoto3.setImageResource(R.drawable.ifpb);
                }

            }
        });
        return root;
    }
}