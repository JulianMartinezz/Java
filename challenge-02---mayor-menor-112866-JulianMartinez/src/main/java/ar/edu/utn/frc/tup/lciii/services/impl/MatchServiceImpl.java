package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.match.MatchResponseDTO;
import ar.edu.utn.frc.tup.lciii.dtos.match.NewMatchRequestDTO;
import ar.edu.utn.frc.tup.lciii.dtos.play.PlayRequestDTO;
import ar.edu.utn.frc.tup.lciii.dtos.play.PlayResponseDTO;
import ar.edu.utn.frc.tup.lciii.entities.MatchEntity;
import ar.edu.utn.frc.tup.lciii.entities.PlayerEntity;
import ar.edu.utn.frc.tup.lciii.models.*;
import ar.edu.utn.frc.tup.lciii.repositories.jpa.MatchJpaRepository;
import ar.edu.utn.frc.tup.lciii.services.DeckService;
import ar.edu.utn.frc.tup.lciii.services.MatchService;
import ar.edu.utn.frc.tup.lciii.services.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchJpaRepository matchJpaRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DeckService deckService;

    @Override
    public List<MatchResponseDTO> getMatchesByPlayer(Long playerId) {
        List<MatchResponseDTO> matches = new ArrayList<>();
        // DO: Implementar el metodo de manera tal que retorne todas las partidas
        //  en las que haya participado un jugador
        Optional<List<MatchEntity>> optionalMatchEntities=matchJpaRepository.getAllByPlayerOneOrPlayerTwo(playerId);
        if(optionalMatchEntities.isPresent()){
            optionalMatchEntities.get().forEach(
                    me -> {matches.add(modelMapper.map(me,MatchResponseDTO.class));}
            );
        }
        return matches;
    }

    @Override
    public MatchResponseDTO createMatch(NewMatchRequestDTO newMatchRequestDTO) {
        Player player1 = playerService.getPlayerById(newMatchRequestDTO.getPlayerOneId());
        Player player2 = playerService.getPlayerById(newMatchRequestDTO.getPlayerTwoId());
        // DO: Terminar de implementar el metodo de manera tal que cree un Match nuevo entre dos jugadores.
        //  Si alguno de los jugadores no existe, la partida no puede iniciarse y debe retornarse una excepcion del tipo
        //  EntityNotFoundException con el mensaje "The user {userId} do not exist"
        //  Cuando se cre el Match, debe crearse el mazo (DeckService.createDeck) y mesclarlo (DeckService.shuffleDeck)
        //  El Match siempre arranca con el playerOne iniciando la partida, con el indice 1 nextCardIndex y lastCard
        //  con la primera carta del mazo y con status PLAYING
        if(Objects.isNull(player1)){
            throw new EntityNotFoundException("The user "+ player1.getId()+" do not exist");
        }
        if(Objects.isNull(player2)){
            throw new EntityNotFoundException("The user "+ player2.getId()+" do not exist");
        }
        Deck deck= deckService.createDeck();
        deckService.shuffleDeck(deck);
        Card card = deckService.takeCard(deck,1);
        MatchEntity matchEntity= new MatchEntity();
        matchEntity.setDeck(deck);
        matchEntity.setLastCard(card);
        matchEntity.setNextCardIndex(1);
        matchEntity.setPlayerOne(modelMapper.map(player1, PlayerEntity.class));
        matchEntity.setPlayerTwo(modelMapper.map(player2, PlayerEntity.class));
        matchEntity.setNextToPlay(modelMapper.map(player1, PlayerEntity.class));
        matchEntity.setMatchStatus(MatchStatus.PLAYING);
        MatchEntity matchEntitySaved = matchJpaRepository.save(matchEntity);
        return modelMapper.map(matchEntitySaved,MatchResponseDTO.class);
    }

    @Override
    public Match getMatchById(Long id) {
        MatchEntity me = matchJpaRepository.getReferenceById(id);
        if(me != null) {
            Match match = modelMapper.map(me, Match.class);
            return match;
        }else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public MatchResponseDTO getMatchResponseDTOById(Long id) {
        MatchEntity me = matchJpaRepository.getReferenceById(id);
        if(me != null) {
            return modelMapper.map(me, MatchResponseDTO.class);
        }else {
            throw new EntityNotFoundException();
        }
    }

    @Transactional
    @Override
    public PlayResponseDTO play(Long matchId, PlayRequestDTO play) {
        PlayResponseDTO playResponseDTO = new PlayResponseDTO();
        Match match = this.getMatchById(matchId);
        // TODO: Terminar de implementar el metodo de manera tal que se ejecute la jugada siguiendo estas reglas:
        //  1 - Si el match no existe disparar una excepcion del tipo EntityNotFoundException
        //      con el mensaje "The match {matchId} do not exist"
        //  2 - Si el jugador no existe disparar una excepcion del tipo EntityNotFoundException
        //      con el mensaje "The user {userId} do not exist"
        //  3 - Si el match ya terminó, disparar una excepcion del tipo MethodArgumentNotValidException
        //      con el mensaje "Game {gameId} is over"
        //  4 - Si el jugador que manda la jugada no es el proximo a jugar, disparar una excepcion del tipo MethodArgumentNotValidException
        //      con el mensaje "It is not the turn of the user {userName}"
        //  5 - Si está OK, ejecutar la jugada haciendo lo siguiente:
        //      5.1 - Tomar el mazo de la partida y buscar la carta que sigue. Usar el metodo DeckService.takeCard
        //      5.2 - Comparar si la carta tomada del mazo es mayor o menor que la ultima carta que se uso.
        //            Usar el metodo privado compareCards() de esta clase.
        //      5.3 - Comparar si el resultado de la comparacion de las cartas se condice con la decición del jugador
        //      5.4 - Si la respuesta es correcta (coinciden) el juego sigue y se debe actualizar
        //            la ultima carta recogida, el proximo jugador en jugar y el proximo indice de carta a recoger
        //      5.5 - Si la respuesta no incorrecta (no coincide) el juego termina y se debe actualizar
        //            la ultima carta recogida, el proximo jugador en jugar, el proximo indice de carta a recoger, el ganador
        //            y el estado de la partida
        //      5.6 - Actualizar el Match
        //  6 - Como respuesta, se deben completar los datos de PlayResponseDTO y retornarlo.
        if(Objects.isNull(match)){
            throw new EntityNotFoundException("The match "+matchId +" do not exist");
        }
        if(Objects.isNull(playerService.getPlayerById(match.getId()))){
            throw new EntityNotFoundException("The user "+match.getId()+" do not exist");
        }
        if(match.getMatchStatus()==MatchStatus.FINISH){
            throw new EntityNotFoundException("Game {gameId} is over");
        }
        if(play.getPlayer() != match.getNextToPlay().getId()){
            throw new EntityNotFoundException("It is not the turn of the user {userName}");
        }
        Card card= new Card();
        card=deckService.takeCard(match.getDeck(),match.getNextCardIndex()+1);
        if(play.getDecision().equals(PlayDecision.MAJOR)){
            if(compareCards(match.getLastCard(),card)==-1){
                playResponseDTO.setPreviousCard(card);
                playResponseDTO.setCardsInDeck(card.getNumber());
                playResponseDTO.setYourCard(match.getLastCard());
                playResponseDTO.setMatchStatus(MatchStatus.PLAYING);
                match.setLastCard(card);
                match.setNextCardIndex(card.getNumber());
                match.setNextToPlay(match.getPlayerOne());
                match.setPlayerOne(match.getPlayerTwo());
                match.setPlayerTwo(match.getPlayerOne());
                matchJpaRepository.save(modelMapper.map(match,MatchEntity.class));
            }
            else {

            }
        }
        return playResponseDTO;

    }

    private Integer compareCards(Card card1, Card card2) {
        // DO: Implementr el metodo de manera tal que retorne:
        //  1 si card1 tiene un valor mayor que card2,
        //  0 si card1 y card2 tienen el mismo valor,
        //  -1 si card1 tiene un valor menor que card 2
        if(card1.getNumber()>card2.getNumber()){
            return 1;
        }
        if(card1.getNumber()<card2.getNumber()){
            return -1;
        } else {
            return 0;
        }
    }
}
