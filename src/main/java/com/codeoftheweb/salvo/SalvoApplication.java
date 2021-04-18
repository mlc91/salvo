package com.codeoftheweb.salvo;
import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository,
                                      GameRepository gamerRepository,
                                      GamePlayerRepository gamePlayerRepository,
                                      ShipRepository shipRepository,
                                      SalvoRepository salvoRepository,
                                      ScoreRepository scoreRepository) {
        return (args) -> {
            //This players, games, gameplayers, etc. were initialized for an example
            //region INICIATION OF PLAYERS
            Player player1 = playerRepository.save(new Player("j.bauer@ctu.gov", passwordEncoder().encode("24")));
            Player player2 = playerRepository.save(new Player("c.obrian@ctu.gov", passwordEncoder().encode("42")));
            Player player3 = playerRepository.save(new Player("t.almeida@ctu.gov", passwordEncoder().encode("kb")));
            Player player4 = playerRepository.save(new Player("kim_bauer@gmail.com", passwordEncoder().encode("mole")));
            //endregion

            //region INICIATION OF GAMES
            Game game1 = gamerRepository.save(new Game());
            Game game2 = gamerRepository.save(new Game());
            Game game3 = gamerRepository.save(new Game());
            Game game4 = gamerRepository.save(new Game());
            Game game5 = gamerRepository.save(new Game());
            Game game6 = gamerRepository.save(new Game());
            Game game7 = gamerRepository.save(new Game());
            Game game8 = gamerRepository.save(new Game());
            //endregion

            //region INICIATION OF GAMEPLAYERS
            GamePlayer gamePlayer1 = gamePlayerRepository.save(new GamePlayer(player1, game1));
            GamePlayer gamePlayer2 = gamePlayerRepository.save(new GamePlayer(player2, game1));
            GamePlayer gamePlayer3 = gamePlayerRepository.save(new GamePlayer(player1, game2));
            GamePlayer gamePlayer4 = gamePlayerRepository.save(new GamePlayer(player2, game2));
            GamePlayer gamePlayer5 = gamePlayerRepository.save(new GamePlayer(player2, game3));
            GamePlayer gamePlayer6 = gamePlayerRepository.save(new GamePlayer(player3, game3));
            GamePlayer gamePlayer7 = gamePlayerRepository.save(new GamePlayer(player1, game4));
            GamePlayer gamePlayer8 = gamePlayerRepository.save(new GamePlayer(player2, game4));
            GamePlayer gamePlayer9 = gamePlayerRepository.save(new GamePlayer(player3, game5));
            GamePlayer gamePlayer10 = gamePlayerRepository.save(new GamePlayer(player1, game5));
            GamePlayer gamePlayer11 = gamePlayerRepository.save(new GamePlayer(player4, game6));
            GamePlayer gamePlayer12 = gamePlayerRepository.save(new GamePlayer(player3, game7));
            GamePlayer gamePlayer13 = gamePlayerRepository.save(new GamePlayer(player3, game8));
            GamePlayer gamePlayer14 = gamePlayerRepository.save(new GamePlayer(player4, game8));
            //endregion

            //region INICIATION OF SHIPS
            List<String> shipPosition;

            Ship ship1 = shipRepository.save(new Ship("destroyer", gamePlayer1, shipPosition = Arrays.asList("H2","H3","H4")));
            Ship ship2 = shipRepository.save(new Ship("submarine", gamePlayer1, shipPosition = Arrays.asList("E1","F1","G1")));
            Ship ship3 = shipRepository.save(new Ship("patrolboat", gamePlayer1, shipPosition = Arrays.asList("B4","B5")));
            Ship ship4 = shipRepository.save(new Ship("destroyer", gamePlayer2, shipPosition = Arrays.asList("B5","C5","D5")));
            Ship ship5 = shipRepository.save(new Ship("patrolboat", gamePlayer2, shipPosition = Arrays.asList("F1","F2")));
            Ship ship6 = shipRepository.save(new Ship("destroyer", gamePlayer3, shipPosition = Arrays.asList("B5", "C5", "D5")));
            Ship ship7 = shipRepository.save(new Ship("patrolboat", gamePlayer3, shipPosition = Arrays.asList("C6", "C7")));
            Ship ship8 = shipRepository.save(new Ship("submarine", gamePlayer4, shipPosition = Arrays.asList("A2", "A3", "A4")));
            Ship ship9 = shipRepository.save(new Ship("patrolboat", gamePlayer4, shipPosition = Arrays.asList("G6", "H6")));
            Ship ship10 = shipRepository.save(new Ship("destroyer", gamePlayer5, shipPosition = Arrays.asList("B5", "C5", "D5")));
            Ship ship11 = shipRepository.save(new Ship("patrolboat", gamePlayer5, shipPosition = Arrays.asList("C6", "C7")));
            Ship ship12 = shipRepository.save(new Ship("submarine", gamePlayer6, shipPosition = Arrays.asList("A2", "A3", "A4")));
            Ship ship13 = shipRepository.save(new Ship("patrolboat", gamePlayer6, shipPosition = Arrays.asList("G6", "H6")));
            Ship ship14 = shipRepository.save(new Ship("destroyer", gamePlayer7, shipPosition = Arrays.asList("B5", "C5", "D5")));
            Ship ship15 = shipRepository.save(new Ship("patrolboat", gamePlayer7, shipPosition = Arrays.asList("C6", "C7")));
            Ship ship16 = shipRepository.save(new Ship("submarine", gamePlayer8, shipPosition = Arrays.asList("A2", "A3", "A4")));
            Ship ship17 = shipRepository.save(new Ship("patrolboat", gamePlayer8, shipPosition = Arrays.asList("G6", "H6")));
            Ship ship18 = shipRepository.save(new Ship("destroyer", gamePlayer9, shipPosition = Arrays.asList("B5", "C5", "D5")));
            Ship ship19 = shipRepository.save(new Ship("patrolboat", gamePlayer9, shipPosition = Arrays.asList("C6", "C7")));
            Ship ship20 = shipRepository.save(new Ship("submarine", gamePlayer10, shipPosition = Arrays.asList("A2", "A3", "A4")));
            Ship ship21 = shipRepository.save(new Ship("patrolboat", gamePlayer10, shipPosition = Arrays.asList("G6", "H6")));
            Ship ship22 = shipRepository.save(new Ship("destroyer", gamePlayer10, shipPosition = Arrays.asList("B5", "C5", "D5")));
            Ship ship23 = shipRepository.save(new Ship("patrolboat", gamePlayer10, shipPosition = Arrays.asList("C6", "C7")));
            Ship ship24 = shipRepository.save(new Ship("destroyer", gamePlayer10, shipPosition = Arrays.asList("B5", "C5", "D5")));
            Ship ship25 = shipRepository.save(new Ship("patrolboat", gamePlayer10, shipPosition = Arrays.asList("C6", "C7")));
            Ship ship26 = shipRepository.save(new Ship("submarine", gamePlayer10, shipPosition = Arrays.asList("A2", "A3", "A4")));
            Ship ship27 = shipRepository.save(new Ship("patrolboat", gamePlayer10, shipPosition = Arrays.asList("G6", "H6")));
            //endregion

            //region INICIATION OF SALVOES
            List<String> salvoPosition;

            Salvo salvo1 = salvoRepository.save(new Salvo(1L, gamePlayer1, salvoPosition = Arrays.asList("B5", "C5", "F1")));
            Salvo salvo2 = salvoRepository.save(new Salvo(1L, gamePlayer2, salvoPosition = Arrays.asList("B4", "B5", "B6")));
            Salvo salvo3 = salvoRepository.save(new Salvo(2L, gamePlayer1, salvoPosition = Arrays.asList("F2", "D5")));
            Salvo salvo4 = salvoRepository.save(new Salvo(2L, gamePlayer2, salvoPosition = Arrays.asList("E1", "H3", "A2")));
            Salvo salvo5 = salvoRepository.save(new Salvo(1L, gamePlayer3, salvoPosition = Arrays.asList("A2", "A4", "G6")));
            Salvo salvo6 = salvoRepository.save(new Salvo(1L, gamePlayer4, salvoPosition = Arrays.asList("B5", "D5", "C7")));
            Salvo salvo7 = salvoRepository.save(new Salvo(2L, gamePlayer3, salvoPosition = Arrays.asList("A3", "H6")));
            Salvo salvo8 = salvoRepository.save(new Salvo(2L, gamePlayer4, salvoPosition = Arrays.asList("C5", "C6")));
            Salvo salvo9 = salvoRepository.save(new Salvo(1L, gamePlayer5, salvoPosition = Arrays.asList("G6", "H6", "A4")));
            Salvo salvo10 = salvoRepository.save(new Salvo(1L, gamePlayer6, salvoPosition = Arrays.asList("H1", "H2", "H3")));
            Salvo salvo11 = salvoRepository.save(new Salvo(2L, gamePlayer5, salvoPosition = Arrays.asList("A2", "A3", "D8")));
            Salvo salvo12 = salvoRepository.save(new Salvo(2L, gamePlayer6, salvoPosition = Arrays.asList("E1", "F2", "G3")));
            Salvo salvo13 = salvoRepository.save(new Salvo(1L, gamePlayer7, salvoPosition = Arrays.asList("A3", "A4", "F7")));
            Salvo salvo14 = salvoRepository.save(new Salvo(1L, gamePlayer8, salvoPosition = Arrays.asList("B5", "C6", "H1")));
            Salvo salvo15 = salvoRepository.save(new Salvo(2L, gamePlayer7, salvoPosition = Arrays.asList("A2", "G6", "H6")));
            Salvo salvo16 = salvoRepository.save(new Salvo(2L, gamePlayer8, salvoPosition = Arrays.asList("C5", "C7", "D5")));
            Salvo salvo17 = salvoRepository.save(new Salvo(1L, gamePlayer9, salvoPosition = Arrays.asList("A1", "A2", "A3")));
            Salvo salvo18 = salvoRepository.save(new Salvo(1L, gamePlayer10, salvoPosition = Arrays.asList("B5", "B6", "C7")));
            Salvo salvo19 = salvoRepository.save(new Salvo(2L, gamePlayer9, salvoPosition = Arrays.asList("G6", "G7", "G8")));
            Salvo salvo20 = salvoRepository.save(new Salvo(2L, gamePlayer10, salvoPosition = Arrays.asList("C6", "D6", "E6")));
            Salvo salvo21 = salvoRepository.save(new Salvo(3L, gamePlayer11, salvoPosition = Arrays.asList("H1", "H8")));
            //endregion

            //region INICIATION OF SCORES
            Score score1 = scoreRepository.save(new Score(1, player1, game1));
            Score score2 = scoreRepository.save(new Score(0, player2, game1));
            Score score3 = scoreRepository.save(new Score(0.5, player1, game2));
            Score score4 = scoreRepository.save(new Score(0.5, player2, game2));
            Score score5 = scoreRepository.save(new Score(1, player2, game3));
            Score score6 = scoreRepository.save(new Score(0, player3, game3));
            Score score7 = scoreRepository.save(new Score(0.5, player1, game4));
            Score score8 = scoreRepository.save(new Score(0.5, player2, game4));
            //endregion
        };
    }
}
//----------------------------------------WEB SECURITY CONFIGURATION CLASSES------------------------------------------//
@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName-> {
            Player player = playerRepository.findByEmail(inputName);
            if (player != null) {
                return new User(player.getEmail(), player.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}
@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/web/**").permitAll()
                .antMatchers("/api/game_view/*").hasAuthority("USER")
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/games").permitAll()
                .and().csrf().ignoringAntMatchers("/h2-console/**")
                .and().headers().frameOptions().sameOrigin();

        http.formLogin()
                .usernameParameter("name")
                .passwordParameter("pwd")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}