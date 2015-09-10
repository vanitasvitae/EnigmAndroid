package de.vanitasvitae.enigmandroid.enigma.inputPreparer;

import android.widget.EditText;

/**
 * Adapter-like connector between text fields and string-outputting-whatever blah
 * Use this to modify the way strings get displayed without modifying the string itself.
 */
public abstract class EditTextAdapter
{
    protected EditText editText;
    protected String content;

    public EditTextAdapter(EditText editText)
    {
        this.editText = editText;
    }

    /**
     * Returns the unmodified text
     * @return content
     */
    public String getText()
    {
        return content;
    }

    public String getModifiedText()
    {
        return editText.getText().toString();
    }

    /**
     * Set the text to both the content and the editText without modifying it
     * @param text text
     */
    public void setRawText(String text)
    {
        this.content = text;
        this.editText.setText(text);
    }

    /**
     * This method needs to be overwritten by the programmer.
     * The coder has to make sure, content gets set to text and also that the editText element
     * gets updated with the modified text
     * @param text text
     */
    public abstract void setText(String text);

    public static EditTextAdapter createEditTextAdapter(EditText editText, String type)
    {
        switch (type)
        {
            case "5": return new EditTextAdapter5Gap(editText);
            case "no": return new EditTextAdapterNoGap(editText);
            default: return new EditTextAdapterNoGap(editText);
        }
    }

    public static class EditTextAdapterNoGap extends  EditTextAdapter
    {
        public EditTextAdapterNoGap(EditText editText)
        {
            super(editText);
        }

        @Override
        public void setText(String text)
        {
            this.content = text;
            this.editText.setText(text);
        }
    }

    public static class EditTextAdapter5Gap extends EditTextAdapter
    {
        public EditTextAdapter5Gap(EditText editText)
        {
            super(editText);
        }

        @Override
        public void setText(String text)
        {
            this.content = text;
            String out = "";
            int i;
            for(i=0; i<text.length()/5; i++)
            {
                out = out + text.substring(i*5, (i+1)*5);
                out = out + " ";
            }
            out = out + text.substring(i*5);
            this.editText.setText(out);
        }
    }
}