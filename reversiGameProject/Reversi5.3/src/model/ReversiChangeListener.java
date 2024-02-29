package model;

/**
 * The ReversiChangeListener interface defines a listener for changes in the game state of Reversi.
 * Implementing this interface allows an object to be notified when changes occur in the game state.
 */
public interface ReversiChangeListener {

  /**
   * This method is called when a change in the game state occurs.
   * Implementing classes should define specific actions to be
   * taken in response to the state change.
   */
  void stateChanged();
}