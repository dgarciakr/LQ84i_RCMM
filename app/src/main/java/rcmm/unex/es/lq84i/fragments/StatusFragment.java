package rcmm.unex.es.lq84i.fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

import rcmm.unex.es.lq84i.R;
import rcmm.unex.es.lq84i.viewmodels.StatusViewModel;
import rcmm.unex.es.lq84i.viewmodels.factories.StatusViewModelFactory;

public class StatusFragment extends Fragment {

    /**
     * ViewModel del fragmento
     */
    private StatusViewModel viewModel;

    /**
     * Vista que contiene los TextViews de los datos
     */
    private View dataHolder;

    /**
     * Actividad anfitriona
     */
    private Activity mHost;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost = getActivity();
        StatusViewModelFactory factory = new StatusViewModelFactory((TelephonyManager)
                Objects.requireNonNull(mHost).getSystemService(Context.TELEPHONY_SERVICE));
        viewModel = ViewModelProviders.of(this, factory).get(StatusViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.status_info, container, false);
        dataHolder = res.findViewById(R.id.data);
        Button b = res.findViewById(R.id.button);
        Button s = res.findViewById(R.id.save_csv);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI();
            }
        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.saveData()) {
                    Snackbar.make(Objects.requireNonNull(mHost.getCurrentFocus()),
                            mHost.getResources().getString(R.string.data_saved),
                            Snackbar.LENGTH_LONG);
                } else {
                    Snackbar.make(Objects.requireNonNull(mHost.getCurrentFocus()),
                            mHost.getResources().getString(R.string.error_saving),
                            Snackbar.LENGTH_LONG);
                }
            }
        });
        return res;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUI();
    }

    private void updateUI() {
        viewModel.updateView(dataHolder, mHost.getResources());
    }
}
