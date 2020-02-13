package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void doesNotGetAttributesTooEasily() {
        // test to make sure we're not setting attributes on all nodes right away
        String body = "<p>One <!-- Two --> Three<![CDATA[Four]]></p>";
        Document doc = Jsoup.parse(body);
        assertFalse(hasAnyAttributes(doc));

        String s = doc.outerHtml();
        assertFalse(hasAnyAttributes(doc));

        Elements els = doc.select("p");
        Element p = els.first();
        assertEquals(1, els.size());
        assertFalse(hasAnyAttributes(doc));

        els = doc.select("p.none");
        assertFalse(hasAnyAttributes(doc));

        String id = p.id();
        assertEquals("", id);
        assertFalse(p.hasClass("Foobs"));
        assertFalse(hasAnyAttributes(doc));

        p.addClass("Foobs");
        assertTrue(p.hasClass("Foobs"));
        assertTrue(hasAnyAttributes(doc));
        assertTrue(hasAnyAttributes(p));

        Attributes attributes = p.attributes();
        assertTrue(attributes.hasKey("class"));
        p.clearAttributes();
        assertFalse(hasAnyAttributes(p));
        assertFalse(hasAnyAttributes(doc));
        assertFalse(attributes.hasKey("class"));
    }

    private boolean hasAnyAttributes(Node node) {
        final boolean[] found = new boolean[1];
        node.filter(new NodeFilter() {
            @Override
            public FilterResult head(Node node, int depth) {
                if (node.hasAttributes()) {
                    found[0] = true;
                    return FilterResult.STOP;
                } else {
                    return FilterResult.CONTINUE;
                }
            }

            @Override
            public FilterResult tail(Node node, int depth) {
                return FilterResult.CONTINUE;
            }
        });
        return found[0];
    }
}
