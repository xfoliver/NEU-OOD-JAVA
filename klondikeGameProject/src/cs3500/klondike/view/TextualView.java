package cs3500.klondike.view;

import java.io.IOException;

/** A marker interface for all text-based views, to be used in the Klondike game. */
public interface TextualView {

  /**
  * Renders a model in some manner (e.g. as text, or as graphics, etc.).
  * @throws IOException if the rendering fails for some reason
  */
  void render() throws IOException;
}
