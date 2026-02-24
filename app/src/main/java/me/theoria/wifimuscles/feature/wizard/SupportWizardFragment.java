package me.theoria.wifimuscles.feature.wizard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.model.SupportWizardModel;

public class SupportWizardFragment extends Fragment {

    private List<SupportWizardModel> steps;
    private int currentIndex = 0;

    private ImageView wizardIcon;
    private TextView wizardTitle;
    private TextView wizardQuestion;
    private Button buttonOptionOne, buttonOptionTwo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support_wizard, container, false);

        steps = SupportWizardManager.getWizardSteps(requireContext());
        bindViews(view);
        showStep(currentIndex);

        return view;
    }

    private void bindViews(View view) {
        wizardIcon = view.findViewById(R.id.wizardIcon);
        wizardTitle = view.findViewById(R.id.wizardTitle);
        wizardQuestion = view.findViewById(R.id.wizardQuestion);
        buttonOptionOne = view.findViewById(R.id.buttonOptionOne);
        buttonOptionTwo = view.findViewById(R.id.buttonOptionTwo);

        buttonOptionOne.setOnClickListener(v -> goToNext(true));
        buttonOptionTwo.setOnClickListener(v -> goToNext(false));
    }

    private void showStep(int index) {
        if (index < 0 || index >= steps.size()) return;

        SupportWizardModel step = steps.get(index);
        currentIndex = index;

        wizardIcon.setImageResource(step.getSupportIcon());
        wizardTitle.setText(step.getTitle());
        wizardQuestion.setText(step.getQuestion());
        buttonOptionOne.setText(step.getOptionOne());

        // Hide second button if string is empty/null
        String optionTwoText = step.getOptionTwo();
        if (optionTwoText == null || optionTwoText.trim().isEmpty()) {
            buttonOptionTwo.setVisibility(View.GONE);
        } else {
            buttonOptionTwo.setVisibility(View.VISIBLE);
            buttonOptionTwo.setText(optionTwoText);
        }
    }

    private void goToNext(boolean choseOptionOne) {
        SupportWizardModel currentStep = steps.get(currentIndex);
        int nextIndex = choseOptionOne ? currentStep.getNextStepIfOptionOne() : currentStep.getNextStepIfOptionTwo();

        if (nextIndex == -1) {
            requireActivity().getSupportFragmentManager().popBackStack(); // Exit
        } else {
            showStep(nextIndex);
        }
    }
}
