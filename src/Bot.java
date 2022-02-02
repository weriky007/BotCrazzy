//Site para rodar esse programa https://blaze.com/pt/games/double
/*
1-[]O BOT BLAZE APOSTAS consiste em detectar
    padrões já implantado dentro dele, e daí em diante
    fazer entradas seguindo o gerenciamento informado
    abaixo!

2-[x]Em todas as entradas, sempre o BOT tem que usar
    apenas 0.05% da banca

3-[x]Quando o BOT fazer entradas em qualquer cor, é
    obrigatório ter uma quantia jogada no branco!
    PORQUE? O branco na minha metodologia serve
    para ter uma segurança caso perca na cor inicial!

4-[x]Então para fazer essa proteção no branco, apenas
    basta dividir o valor principal por dez e fazer uma
    entrada com o valor da divisão no branco,

5-[]Apeenas fazer entradas no branco quando o BOT já estiver fazendo
    entradas de R$2,00 acima, nunca abaixo disso!

6-[x]Iplementar os seguintes padroes para iniciar as apostas
    preto, preto, preto, preto, preto = vermelho
    vermelho, vermelho, vermelho, vermelho, vermelho = preto
    preto, vermelho, preto, vermelho, preto = vermelho
    vermelho, preto, vermelho, preto, vermelho = preto

7-[]Caso as apostas sejam abaixo de $2 nao considere a aposta na branca
    nao achou um dos padroes ou mesmo com o padrao errou.
    martingale na aposta e na branca, caso errar de  novo
    martingale na aposta e na branca, caso errar de novo reinicia o valor normal

 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
//======================================================================================================================
public class Bot {
    //CRIA REFERENCIA DO ROBOT
    Robot robot;
    {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    //VALORES EM RGB DAS CORES
    Color pretoTela  = new Color(38, 47, 60);
    Color vermelhoTela = new Color(240, 44, 76);
    Color brancoTela = new Color(254, 254, 254);

    //MULTIPLOS APOSTAS
    double xPreto = 2;
    double xVermelho = 2;
    double xBranco = 14;

    //SEQUENCIAS
    int vermelho = 1;
    int preto = 2;
    int branco = 3;

    int[] sequencia1 = {preto, preto, preto, preto, preto}; //APOSTA VERMELHO
    int[] sequencia2 = {vermelho, vermelho, vermelho, vermelho, vermelho}; //APOSTA PRETO
    int[] sequencia3 = {preto, vermelho, preto, vermelho, preto}; //APOSTA VERMELHO
    int[] sequencia4 = {vermelho, preto, vermelho, preto, vermelho}; //APOSTA PRETO

    int[] sequencia5 = {vermelho, vermelho, vermelho}; //APOSTA PRETO
    int[] sequencia6 = {preto, preto, preto}; //APOSTA VERMELHO

    //POSICOES XY DOS ULTIMOS 17 RESULTADOS DA ROLETA
    int[] resultadosAnterioresX = {1220, 1180, 1140, 1100, 1060, 1020, 980, 940, 900, 860, 820, 780, 740, 700, 660, 620, 580};
    int resultadosAnterioresY = 598;

    //ATRIBUTOS DA BARRA DE LOADING VERMELHA
    int[] barraVermelhaCOR = {240, 44, 76};
    int barraVermelhaTempo = 16;
    int barraVermelhaX = 556;
    int barraVermelhaY = 244;
//======================================================================================================================
    //METODO QUE EXECUTA CODIGO JAVA
    public static void main(String[]args) throws AWTException, IOException, InterruptedException {
        Bot bot = new Bot();
        bot.execute();
    }
//======================================================================================================================
    //EXECUTA O ALGORITMO
    public void execute() throws InterruptedException, AWTException, IOException {
        configuraCapturaDeTela();
        int flag = 0;
        double deposito = 100;
        double saldo = deposito;
        double percentual = 0.005;
        double valorAposta = deposito*percentual;
        int multiploMatingale = 1;
        double apostaBranco = (valorAposta/10)*multiploMatingale;



        //INICIO DO LACO
        while (flag  == 0){

            Color corEscolhida = pretoTela;
            Color corBranco = brancoTela;
            Color corResultado = robot.getPixelColor(resultadosAnterioresX[0], resultadosAnterioresY);


            saldo = apostas(saldo, valorAposta, multiploMatingale, apostaBranco, corEscolhida, corBranco, corResultado);


            System.out.println("---------------------------------------------------------------------------------------");
            TimeUnit.SECONDS.sleep(3);
        }//FIM DO LACO
    }//FIM EXECUTE

    //REALIZA E VERIFICA APOSTAS
    private double apostas(double saldo, double valorAposta, int multiploMatingale, double apostaBranco, Color corEscolhida, Color corBranco, Color corResultado) {
        //VERIFICA SE APOSTA == COR RESULTADO (PRETO OU VERMELHO)
        if (corEscolhida.equals(corResultado)){
            saldo = saldo + (xPreto * valorAposta - apostaBranco);//SE SUBTRAI O VALOR APOSTADO NO BRANCO
            System.out.println("Acertou: R$"+ saldo);
        }
        //VERIFICA SE APOSTA != RESULTADO (PRETO OU VERMELHO)
        else if (!corEscolhida.equals(corResultado)){
            saldo = saldo - valorAposta;

            //JA QUE APOSTA(PRETO OU VERMELHO) NAO SAIU PODE SAIR BRANCO. VERIFICA APOSTA BRANCO
            if(corBranco.equals(corResultado)){
                saldo = saldo + ((xBranco * multiploMatingale)* apostaBranco);
                System.out.println("Acertou 14x: R$"+ saldo);
            }
            else if(!corBranco.equals(corResultado)){
                saldo = saldo - apostaBranco;
                System.out.println("Errou: R$"+ saldo);
            }
        }
        return saldo;
    }

    //VERIFICA CORES
    public Object verificaCores(int X, int Y, int position) throws AWTException, InterruptedException {
        Color corColetada = robot.getPixelColor(X, Y);

        if(corColetada.equals(pretoTela)){
            position+=1;
            System.out.println(position+": Preto");
        }else if(corColetada.equals(vermelhoTela)){
            position+=1;
           System.out.println(position+": Vermelho");
        }else if(corColetada.equals(brancoTela)){
           position+=1;
           System.out.println(position+": Branco");
        }
        return null;
    }

    //VERIFICA AS CORES SORTEADAS ANTERRIORMENTE
    public void verificaSorteadas() throws AWTException {
        for(int position =0;position<resultadosAnterioresX.length;position++){
            try {
                verificaCores(resultadosAnterioresX[position], resultadosAnterioresY, position);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //CAPTURA TELA
    private static void configuraCapturaDeTela() throws AWTException, IOException {
        Robot robot = new Robot();
        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
        File file = new File("screen-capture.bmp");
        boolean status = ImageIO.write(bufferedImage, "bmp", file);
        System.out.println("Screen Captured ? " + status + " File Path:- " + file.getAbsolutePath());
    }
//======================================================================================================================
}
