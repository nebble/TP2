/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tp2crypto;

/**
 *
 * @author carlbelanger
 */
public enum Status {
    ConnectionClosed, WaitingForConnection, ServerConnection, ClientConnected, 
    Negociating, TrustEstablished, AtemptingLogging, Authenticate, Logged, ClientLogged
}
