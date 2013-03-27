package ca.keithzg.paulmiller.is.offline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoFragment extends Fragment {

	public InfoFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            // No container, no content.
            return null;
        }
        View v = inflater.inflate(R.layout.frag_info, null);

	    Linkify.addLinks(((TextView) v.findViewById(R.id.textView)), Pattern.compile("The Verge"), "", null, new VergeLinker("http://www.theverge.com"));
	    Linkify.addLinks(((TextView) v.findViewById(R.id.textView)), Pattern.compile("offline journey"), "", null, new VergeLinker("http://www.theverge.com/users/futurepaul/blog"));

        return v;
    }

	public class VergeLinker implements Linkify.TransformFilter {

		private String url;

		public VergeLinker(String url) {
			this.url = url;
		}

		@Override
		public String transformUrl(Matcher match, String url) {
			return this.url;
		}
	}
    
}
