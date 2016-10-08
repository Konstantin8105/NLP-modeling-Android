package com.modelingbrain.home.model;

import com.modelingbrain.home.R;

public enum ModelID {
    ID_Science(0),//OK
    ID_SCORE(10),//OK
    ID_SMART(20),//OK
    ID_TOTE(30),//OK
    ID_TricksOfLanguage(40),//OK
    ID_MiltonModel(50),//OK
    ID_Polar(60),//OK
    ID_Eye(70),//OK
    ID_AIDA(80),//OK
    ID_FrameOfProposal(90),//OK
    ID_ElementsOfAnchors(100),//OK
    //ID_UoltDisney(11),//NONE
    ID_VAKOG(120),//OK
    ID_PROFILE2(130),//OK
    ID_NOTE(140),//OK
    ID_Strategy_VKDiss(150),//OK
    ID_Strategy_ConflictParts(160),//OK
    ID_Strategy_XCP(170),//OK
    ID_Strategy_Anchor(180),//OK
    ID_PROFILE3(190),//OK
    ID_Three_Position(200),//OK
    ID_Karpman(210),//OK
    ID_Modeling_Gordon(220),//OK
    ID_Modeling_Gordon_Short(230),//OK
    ID_State(240),//OK
    ID_Rules(250)//OK
    ;

    private final int parameter;

    ModelID(int parameter) {
        this.parameter = parameter;
    }

    public int getParameter() {
        return parameter;
    }

    public ModelType getModelType() {
        return arrModelLine[getPositionArrayModelLine()].modelType;
    }

    public int getSize() {
        return arrModelLine[getPositionArrayModelLine()].modelSize;
    }

    public int getResourceIcon() {
        return arrModelLine[getPositionArrayModelLine()].resourceIcon;
    }

    public int getResourceQuestion() {
        return arrModelLine[getPositionArrayModelLine()].resourceQuestions;
    }

    public boolean isOldModel() {
        return arrModelLine[getPositionArrayModelLine()].oldModel;
    }

    private int getPositionArrayModelLine() {
        int i = 0;
        for (; i < arrModelLine.length; i++) {
            if (arrModelLine[i].modelID == this)
                break;
        }
        return i;
    }

    static final class ModelLine {
        final ModelID modelID;
        final int resourceQuestions;
        final ModelType modelType;
        final int resourceIcon;
        final int modelSize;
        final boolean oldModel;

        @SuppressWarnings("SameParameterValue")
        ModelLine(ModelID modelID, int resourceQuestions, ModelType modelType, int resourceIcon, int modelSize, boolean oldModel) {
            this.modelID = modelID;
            this.resourceQuestions = resourceQuestions;
            this.modelType = modelType;
            this.resourceIcon = resourceIcon;
            this.modelSize = modelSize;
            this.oldModel = oldModel;
        }

        @SuppressWarnings("SameParameterValue")
        ModelLine(ModelID modelID, int resourceQuestions, ModelType modelType, int resourceIcon, int modelSize) {
            this.modelID = modelID;
            this.resourceQuestions = resourceQuestions;
            this.modelType = modelType;
            this.resourceIcon = resourceIcon;
            this.modelSize = modelSize;
            this.oldModel = false;
        }
    }

    static final ModelLine[] arrModelLine = {

            new ModelLine(ModelID.ID_Modeling_Gordon,
                    R.array.str_Left_Modeling_Gordon,
                    ModelType.MODEL,
                    R.drawable.ic_model_gordon,
                    12),

            new ModelLine(ModelID.ID_Modeling_Gordon_Short,
                    R.array.str_Left_Modeling_Gordon_Short,
                    ModelType.DARK,
                    R.drawable.ic_model_gordon_short,
                    4),

            new ModelLine(ModelID.ID_Rules,
                    R.array.str_Left_Rules,
                    ModelType.DARK,
                    R.drawable.ic_model_rules,
                    1),

            new ModelLine(ModelID.ID_Science,
                    R.array.str_Left_Science,
                    ModelType.MODEL,
                    R.drawable.ic_model_science,
                    5),

            new ModelLine(ModelID.ID_SCORE,
                    R.array.str_Left_SCORE,
                    ModelType.MODEL,
                    R.drawable.ic_model_score,
                    5),

            new ModelLine(ModelID.ID_SMART,
                    R.array.str_Left_SMART,
                    ModelType.MODEL,
                    R.drawable.ic_model_smart,
                    5),

            new ModelLine(ModelID.ID_TOTE,
                    R.array.str_Left_TOTE,
                    ModelType.MODEL,
                    R.drawable.ic_model_tote,
                    4),

            new ModelLine(ModelID.ID_TricksOfLanguage,
                    R.array.str_Left_TricksOfLanguage,
                    ModelType.MODEL,
                    R.drawable.ic_model_tricksoflanguage,
                    14),

            new ModelLine(ModelID.ID_MiltonModel,
                    R.array.str_Left_MiltonModel,
                    ModelType.MODEL,
                    R.drawable.ic_model_milton,
                    15),

            new ModelLine(ModelID.ID_Eye,
                    R.array.str_Left_Eye,
                    ModelType.MODEL,
                    R.drawable.ic_model_eye,
                    1),

            new ModelLine(ModelID.ID_Polar,
                    R.array.str_Left_Polar,
                    ModelType.MODEL,
                    R.drawable.ic_model_polar,
                    8),

            new ModelLine(ModelID.ID_AIDA,
                    R.array.str_Left_AIDA,
                    ModelType.MODEL,
                    R.drawable.ic_model_aida,
                    4),

            new ModelLine(ModelID.ID_ElementsOfAnchors,
                    R.array.str_Left_ElementsOfAnchors,
                    ModelType.MODEL,
                    R.drawable.ic_model_anchor_elements,
                    7),

            new ModelLine(ModelID.ID_VAKOG,
                    R.array.str_Left_VAK,
                    ModelType.MODEL,
                    R.drawable.ic_model_vak,
                    3),

            new ModelLine(ModelID.ID_PROFILE2,
                    R.array.str_Left_PROFILE2,
                    ModelType.DARK,
                    R.drawable.ic_model_profile2,
                    8, true),

            new ModelLine(ModelID.ID_PROFILE3,
                    R.array.str_Left_PROFILE3,
                    ModelType.DARK,
                    R.drawable.ic_model_profile3,
                    10),

            new ModelLine(ModelID.ID_NOTE,
                    R.array.str_Left_Note,
                    ModelType.NONE,
                    R.drawable.ic_model_note,
                    1),

            new ModelLine(ModelID.ID_Strategy_VKDiss,
                    R.array.str_Left_Strategy_VKDiss,
                    ModelType.MODEL,
                    R.drawable.ic_model_vk_dissiciation,
                    9),

            new ModelLine(ModelID.ID_Strategy_ConflictParts,
                    R.array.str_Left_Strategy_ConflictParts,
                    ModelType.MODEL,
                    R.drawable.ic_model_conflict_parts,
                    6),

            new ModelLine(ModelID.ID_Strategy_XCP,
                    R.array.str_Left_Strategy_XCP,
                    ModelType.MODEL,
                    R.drawable.ic_model_frame_of_goal,
                    9),

            new ModelLine(ModelID.ID_Strategy_Anchor,
                    R.array.str_Left_Strategy_Anchor,
                    ModelType.MODEL,
                    R.drawable.ic_model_anchor,
                    5),

            new ModelLine(ModelID.ID_Three_Position,
                    R.array.str_Left_Three_Position,
                    ModelType.MODEL,
                    R.drawable.ic_model_perceptual_positions,
                    4),

            new ModelLine(ModelID.ID_Karpman,
                    R.array.str_Left_Karpman,
                    ModelType.MODEL,
                    R.drawable.ic_model_karpman,
                    3),

            new ModelLine(ModelID.ID_State,
                    R.array.str_Left_State,
                    ModelType.MODEL,
                    R.drawable.ic_model_state,
                    2),

            new ModelLine(ModelID.ID_FrameOfProposal,
                    R.array.str_Left_FrameOfProposal,
                    ModelType.MODEL,
                    R.drawable.ic_model_frame_of_proposal,
                    7)
    };
}