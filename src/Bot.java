//Site para rodar esse programa https://blaze.com/pt/games/double
/*

1-[x]Iplementar os seguintes padroes para iniciar as apostas
    preto, preto, preto, preto, preto = vermelho
    vermelho, vermelho, vermelho, vermelho, vermelho = preto
    preto, vermelho, preto, vermelho, preto = vermelho
    vermelho, preto, vermelho, preto, vermelho = preto

2-[X]O BOT BLAZE APOSTAS consiste em detectar
    padrões já implantado dentro dele, e daí em diante
    fazer entradas seguindo o gerenciamento informado
    abaixo!

3-[x]Em todas as entradas, sempre o BOT tem que usar
    apenas 0.05% da banca

4-[x]Quando o BOT fazer entradas em qualquer cor, é
    obrigatório ter uma quantia jogada no branco!
    PORQUE? O branco na minha metodologia serve
    para ter uma segurança caso perca na cor inicial!

5-[x]Então para fazer essa proteção no branco, apenas
    basta dividir o valor principal por dez e fazer uma
    entrada com o valor da divisão no branco,

6-[x]Apeenas fazer entradas no branco quando o BOT já estiver fazendo
    entradas de R$2,00 acima, nunca abaixo disso!

7-[x]Caso as apostas sejam abaixo de $2 desconsidere a aposta na branca
    nao achou um dos padroes ou mesmo com o padrao errou.
    martingale na aposta e na branca, caso errar de  novo
    martingale na aposta e na branca, caso errar de novo reinicia o valor normal
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
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
    //RGBS PARA ANALISE DE VARIACOES
    int rPreto =38;
    int gPreto =47;
    int bPreto =60;

    int rVermelho = 240;
    int gVermelho = 44;
    int bVermelho = 76;

    int rBranco = 254;
    int gBranco = 254;
    int bBranco = 254;


    //VALORES EM RGB DAS CORES DOS RESULTADOS
    Color pretoTela  = new Color(rPreto, gPreto, bPreto);
    Color vermelhoTela = new Color(rVermelho, gVermelho, bVermelho);
    Color brancoTela = new Color(rBranco, gBranco, bBranco);

    //RGB DO BOTAO QUE FAZ A APOSTA QUANDO ELE ESTA LIBERADO EM VERMELHO VIVO
    Color rgbBotaoConfirmaAposta = new Color(241,44,76);
    //COLETA A COR DO BOTA PRA VER SE ELE ESTA LIBERADO, ALTERAR AS COORDENADAS DE ACORDO COM A RESOLUCAO
    Color coordenadasBotaoConfirmaAposta = robot.getPixelColor(460, 400);

    //BARRA VERMELHA

    //MULTIPLOS APOSTAS
    double xPreto = 2;
    double xVermelho = 2;
    double xBranco = 14;

    //PARAMETRO PRA VER SE ELE CLICA NO BOTAO REALIZAR APOSTA, SE estadoDoBotao == 1  QUER DIZER QUE O BOTAO NAO ESTA LIBERADO
    int verificaEstadoDoBotaoRealizaAposta =0;
    int verificaEscolhaAposta =0;
    int verificaResultadoAposta =0;

    //PADROES DE CORES
    Color[] sequencia1 = {pretoTela, pretoTela, pretoTela, pretoTela, pretoTela}; //APOSTA VERMELHO
    Color[] sequencia2 = {pretoTela, vermelhoTela, pretoTela, vermelhoTela, pretoTela}; //APOSTA VERMELHO
    Color[] sequencia3 = {pretoTela, pretoTela, pretoTela, pretoTela}; //APOSTA VERMELHO
    Color[] sequencia4 = {pretoTela, pretoTela, pretoTela}; //APOSTA VERMELHO

    Color[] sequencia5 = {vermelhoTela, vermelhoTela, vermelhoTela, vermelhoTela, vermelhoTela}; //APOSTA PRETO
    Color[] sequencia6 = {vermelhoTela, pretoTela, vermelhoTela, pretoTela,vermelhoTela}; //APOSTA PRETO
    Color[] sequencia7 = {vermelhoTela, vermelhoTela, vermelhoTela, vermelhoTela}; //APOSTA PRETO
    Color[] sequencia8 = {vermelhoTela, vermelhoTela, vermelhoTela}; //APOSTA PRETO


    //POSICOES XY DOS ULTIMOS 16 RESULTADOS DA ROLETA
    int[] resultadosAnterioresX = {1190, 1150, 1110, 1070, 1030, 990, 950, 910, 870, 830, 790, 750, 710, 670, 630, 590};
    int resultadosAnterioresY = 600;

//======================================================================================================================
    //METODO QUE EXECUTA CODIGO JAVA
    public static void main(String[]args) throws AWTException, IOException, InterruptedException {
        Bot bot = new Bot();
        bot.execute();
    }
//======================================================================================================================
    //EXECUTA O ALGORITMO
    public void execute() throws InterruptedException, AWTException, IOException {
//        configuraCapturaDeTela();
//        verificaSorteadas();
        int flag = 0;
        //valor do deposito = vc escolhe
        double deposito = 398;
        double saldo = deposito;
        double percentual = 0.005;
        double valorAposta = deposito*percentual;
        int xMartingale = 1;
        int multiploMatingale = 1;
        double apostaBranco = (valorAposta/10)*multiploMatingale;

        int todasCoresParaEscolherAposta = 0;

        Random apostaAleatoria = new Random();

        int vermelho = 1;
        int preto = 2;
        int branco = 3;


        int qtdAposta = 0;
        TimeUnit.SECONDS.sleep(1);

        //INICIO DO LACO PRINCIPAL
        while (flag  == 0) {
                Color corEscolhida = null;
                Color corBranco = brancoTela;

                //CORRIGINDO O VALOR MINIMO DO JOGO
                if (valorAposta < 2) {
                    valorAposta = 2;
                }


                   //COLETAR OS 5 ULTIMOS RESULTADOS PARA REALIZAR A APOSTA
                   Color[] cincoUltasCores = new Color[5];
                   System.out.println("=================================================================================");
                   System.out.println("Verificando resultados anteriores");
                   for (int a = 0; a < 5; a++) {
                       cincoUltasCores[a] = robot.getPixelColor(resultadosAnterioresX[a], resultadosAnterioresY);
                   }
//----------------------------------------------------------------------------------------------------------------------
                   //VERIFICAR SEQUENCIA1
                   if (cincoUltasCores[0].equals(sequencia1[0])) {
                       if (cincoUltasCores[1].equals(sequencia1[1])) {
                           if (cincoUltasCores[2].equals(sequencia1[2])) {
                               if (cincoUltasCores[3].equals(sequencia1[3])) {
                                   if (cincoUltasCores[4].equals(sequencia1[4])) {
                                       valorAposta = valorAposta*2;
                                       corEscolhida = vermelhoTela;
                                       todasCoresParaEscolherAposta = vermelho;
                                   }
                               }
                           }
                       }
                   //RANDOM
                   } else {
                       valorAposta = 2;
                       verificaValorAposta(valorAposta, todasCoresParaEscolherAposta, apostaAleatoria);
                       if (todasCoresParaEscolherAposta == 1) {
                           corEscolhida = vermelhoTela;
                       } else if (todasCoresParaEscolherAposta == 2) {
                           corEscolhida = pretoTela;
                       }
                   }
//----------------------------------------------------------------------------------------------------------------------
                   //VERIFICA SEQUENCIA2
                   if (cincoUltasCores[0].equals(sequencia2[0])) {
                       if (cincoUltasCores[1].equals(sequencia2[1])) {
                           if (cincoUltasCores[2].equals(sequencia2[2])) {
                               if (cincoUltasCores[3].equals(sequencia2[3])) {
                                   if (cincoUltasCores[4].equals(sequencia2[4])) {
                                       valorAposta = valorAposta*2;
                                       corEscolhida = vermelhoTela;
                                       todasCoresParaEscolherAposta = vermelho;
                                   }
                               }
                           }
                       }
                   //RANDOM
                   } else {
                       valorAposta = 2;
                       verificaValorAposta(valorAposta, todasCoresParaEscolherAposta, apostaAleatoria);
                       if (todasCoresParaEscolherAposta == 1) {
                           corEscolhida = vermelhoTela;
                       } else if (todasCoresParaEscolherAposta == 2) {
                           corEscolhida = pretoTela;
                       }
                   }
//----------------------------------------------------------------------------------------------------------------------
                   //VERIFICA SEQUENCIA3
                   if (cincoUltasCores[0].equals(sequencia3[0])) {
                       if (cincoUltasCores[1].equals(sequencia3[1])) {
                           if (cincoUltasCores[2].equals(sequencia3[2])) {
                               if (cincoUltasCores[3].equals(sequencia3[3])) {
                                   valorAposta = valorAposta*2;
                                   corEscolhida = vermelhoTela;
                                   todasCoresParaEscolherAposta = vermelho;
                               }
                           }
                       }
                   //RANDOM
                   } else {
                       valorAposta = 2;
                       verificaValorAposta(valorAposta, todasCoresParaEscolherAposta, apostaAleatoria);
                       if (todasCoresParaEscolherAposta == 1) {
                           corEscolhida = vermelhoTela;
                       } else if (todasCoresParaEscolherAposta == 2) {
                           corEscolhida = pretoTela;
                       }
                   }
//----------------------------------------------------------------------------------------------------------------------
                   //VERIFICA SEQUENCIA4
                   if (cincoUltasCores[0].equals(sequencia4[0])) {
                       if (cincoUltasCores[1].equals(sequencia4[1])) {
                           if (cincoUltasCores[2].equals(sequencia4[2])) {
                               valorAposta = valorAposta*2;
                               corEscolhida = vermelhoTela;
                               todasCoresParaEscolherAposta = vermelho;
                           }
                       }
                   //RANDOM
                   } else {
                       valorAposta = 2;
                       verificaValorAposta(valorAposta, todasCoresParaEscolherAposta, apostaAleatoria);
                       if (todasCoresParaEscolherAposta == 1) {
                           corEscolhida = vermelhoTela;
                       } else if (todasCoresParaEscolherAposta == 2) {
                           corEscolhida = pretoTela;
                       }
                   }
//----------------------------------------------------------------------------------------------------------------------
            //VERIFICAR SEQUENCIA5
            if (cincoUltasCores[0].equals(sequencia5[0])) {
                if (cincoUltasCores[1].equals(sequencia5[1])) {
                    if (cincoUltasCores[2].equals(sequencia5[2])) {
                        if (cincoUltasCores[3].equals(sequencia5[3])) {
                            if (cincoUltasCores[4].equals(sequencia5[4])) {
                                valorAposta = valorAposta*2;
                                corEscolhida = pretoTela;
                                todasCoresParaEscolherAposta = preto;
                            }
                        }
                    }
                }
                //RANDOM
            } else {
                valorAposta = 2;
                verificaValorAposta(valorAposta, todasCoresParaEscolherAposta, apostaAleatoria);
                if (todasCoresParaEscolherAposta == 1) {
                    corEscolhida = vermelhoTela;
                } else if (todasCoresParaEscolherAposta == 2) {
                    corEscolhida = pretoTela;
                }
            }
//----------------------------------------------------------------------------------------------------------------------
            //VERIFICA SEQUENCIA6
            if (cincoUltasCores[0].equals(sequencia6[0])) {
                if (cincoUltasCores[1].equals(sequencia6[1])) {
                    if (cincoUltasCores[2].equals(sequencia6[2])) {
                        if (cincoUltasCores[3].equals(sequencia6[3])) {
                            if (cincoUltasCores[4].equals(sequencia6[4])) {
                                valorAposta = valorAposta*2;
                                corEscolhida = pretoTela;
                                todasCoresParaEscolherAposta = preto;
                            }
                        }
                    }
                }
                //RANDOM
            } else {
                valorAposta = 2;
                verificaValorAposta(valorAposta, todasCoresParaEscolherAposta, apostaAleatoria);
                if (todasCoresParaEscolherAposta == 1) {
                    corEscolhida = vermelhoTela;
                } else if (todasCoresParaEscolherAposta == 2) {
                    corEscolhida = pretoTela;
                }
            }
//----------------------------------------------------------------------------------------------------------------------
            //VERIFICA SEQUENCIA7
            if (cincoUltasCores[0].equals(sequencia7[0])) {
                if (cincoUltasCores[1].equals(sequencia7[1])) {
                    if (cincoUltasCores[2].equals(sequencia7[2])) {
                        if (cincoUltasCores[3].equals(sequencia7[3])) {
                            valorAposta = valorAposta*2;
                            corEscolhida = pretoTela;
                            todasCoresParaEscolherAposta = preto;
                        }
                    }
                }
                //RANDOM
            } else {
                valorAposta = 2;
                verificaValorAposta(valorAposta, todasCoresParaEscolherAposta, apostaAleatoria);
                if (todasCoresParaEscolherAposta == 1) {
                    corEscolhida = vermelhoTela;
                } else if (todasCoresParaEscolherAposta == 2) {
                    corEscolhida = pretoTela;
                }
            }
//----------------------------------------------------------------------------------------------------------------------
            //VERIFICA SEQUENCIA8
            if (cincoUltasCores[0].equals(sequencia8[0])) {
                if (cincoUltasCores[1].equals(sequencia8[1])) {
                    if (cincoUltasCores[2].equals(sequencia8[2])) {
                        valorAposta = valorAposta*2;
                        corEscolhida = pretoTela;
                        todasCoresParaEscolherAposta = preto;
                    }
                }
            //RANDOM
            } else {
                valorAposta = 2;
                verificaValorAposta(valorAposta, todasCoresParaEscolherAposta, apostaAleatoria);
                if (todasCoresParaEscolherAposta == 1) {
                    corEscolhida = vermelhoTela;
                } else if (todasCoresParaEscolherAposta == 2) {
                    corEscolhida = pretoTela;
                }
            }
//----------------------------------------------------------------------------------------------------------------------

            //LIMITA O MULTIPLO
            if(valorAposta > 8){
                valorAposta = 2;
            }

            //CON1.0FIGURA valor a ser digitado da aposta
            String digiteAposta = Double.toString(valorAposta);
            StringSelection stringSelecionada = new StringSelection(digiteAposta);//SELECIONA A STRING
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelecionada, stringSelecionada);

            //VERIFICA O VALOR DA APOSTA PARA INSERI O BRANCO OU NAO
            todasCoresParaEscolherAposta = verificaValorAposta(valorAposta, todasCoresParaEscolherAposta, apostaAleatoria);


                while(verificaEscolhaAposta <1) {
                 if (coordenadasBotaoConfirmaAposta.equals(rgbBotaoConfirmaAposta)) {
//                     System.out.println("Realizando cliques escolhas");
                   //VERIFICA A APOSTA ESCOLHIDA PARA REALIZAR AS ACOES NO SITE
                   if (todasCoresParaEscolherAposta == preto) {
                       String p = digiteAposta;
                       robot.mouseMove(287, 232);//CAIXA DIGITA VALOR APOSTA
                       TimeUnit.SECONDS.sleep(1);
                       robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);//APERTA BOTAO ESQUERDO DO MOUSE
                       robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);//SOLTA BOTAO ESQUERDO DO MOUSE
                       robot.keyPress(KeyEvent.VK_CONTROL);
                       robot.keyPress(KeyEvent.VK_V);
                       robot.keyRelease(KeyEvent.VK_CONTROL);
                       robot.keyRelease(KeyEvent.VK_V);
                       corEscolhida = pretoTela;
                       robot.mouseMove(472, 328);//POSICAO DO BOTAO PRETO
                       robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);//APERTA BOTAO ESQUERDO DO MOUSE
                       robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);//SOLTA BOTAO ESQUERDO DO MOUSE
                       System.out.println("Cor ecolhida: preto"+" | "+"Valor da aposta: R$"+valorAposta+" | "+"Quantidade de apostas: " + (qtdAposta += 1));
                   } else if (todasCoresParaEscolherAposta == vermelho) {
                       String v = digiteAposta;
                       robot.mouseMove(287, 232);//CAIXA DIGITA VALOR APOSTA
                       TimeUnit.SECONDS.sleep(1);
                       robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);//APERTA BOTAO ESQUERDO DO MOUSE
                       robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);//SOLTA BOTAO ESQUERDO DO MOUSE
                       robot.keyPress(KeyEvent.VK_CONTROL);
                       robot.keyPress(KeyEvent.VK_V);
                       robot.keyRelease(KeyEvent.VK_CONTROL);
                       robot.keyRelease(KeyEvent.VK_V);
                       corEscolhida = vermelhoTela;
                       robot.mouseMove(272, 333); //POSICAO DO BOTAO VERMELHO
                       robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);//APERTA BOTAO ESQUERDO DO MOUSE
                       robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);//SOLTA BOTAO ESQUERDO DO MOUSE
                       System.out.println("Cor ecolhida: vermelho"+" | "+"Valor da aposta: R$"+valorAposta+" | "+"Quantidade de apostas: " + (qtdAposta += 1));
                   } else if (todasCoresParaEscolherAposta == branco) {
                       String v = digiteAposta;
                       TimeUnit.SECONDS.sleep(1);
                       robot.mouseMove(287, 232);//CAIXA DIGITA VALOR APOSTA
                       robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);//APERTA BOTAO ESQUERDO DO MOUSE
                       robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);//SOLTA BOTAO ESQUERDO DO MOUSE
                       robot.keyPress(KeyEvent.VK_CONTROL);
                       robot.keyPress(KeyEvent.VK_V);
                       robot.keyRelease(KeyEvent.VK_CONTROL);
                       robot.keyRelease(KeyEvent.VK_V);
                       corEscolhida = brancoTela;
                       robot.mouseMove(372, 337);//POSICAO DO BOTAO BRANCO
                       robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);//APERTA BOTAO ESQUERDO DO MOUSE
                       robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);//SOLTA BOTAO ESQUERDO DO MOUSE
                       System.out.println("Cor ecolhida: branco"+" | "+"Valor da aposta: R$"+valorAposta+" | "+" Quantidade de apostas: " + (qtdAposta += 1));
                   }
                       verificaEscolhaAposta = 1;
               }//FIM IF
               }
//======================================================================================================================
                //LACO QUE VERIFICA A COR DO BOTAO PARA REALIZAR A APOSTA
                //ELE FICA PRESO AQUI ATE O BOTAO LIBERAR NA TELA
                while (verificaEstadoDoBotaoRealizaAposta < 1) {
                    realizaClickBotaoAposta();
                }

                //INICIA A VERIFICACAO DO RESULTADO
                while (verificaResultadoAposta <1) {
                    Color coordenadasBarra = robot.getPixelColor(1198, 247);
                    Color rgbBarraConfirmaVerificacao = new Color(241,44,76);
//                    System.out.println("Dentro da verificacao de resultado");

                    if (coordenadasBarra.equals(rgbBarraConfirmaVerificacao)) {
                        Color corResultado = robot.getPixelColor(resultadosAnterioresX[0], resultadosAnterioresY);
//                        System.out.println("cor teste"+corResultado);
                        //USADO PARA VERIFICAR COR QUE SERA IMPRESSA  NO TERMINAL
                        String re = "";
                        if (corResultado.equals(pretoTela)) {
                            re = "Preto  ";
                        }

                        if (corResultado.equals(vermelhoTela)) {
                            re = "Vermelho  ";
                        }

                        if (corResultado.equals(brancoTela)) {
                            re = "Branco  ";
                        }

                        //COMO O VALOR DA APOSTA E MAIOR DO QUE 20  ELE APOSTA NAS BRANCAS
                        if (valorAposta >= 20) {
                            //VERIFICA SE APOSTA == COR RESULTADO (PRETO OU VERMELHO)
                            if (corEscolhida.equals(corResultado)) {
                                saldo = saldo + xPreto * valorAposta - apostaBranco;//SE SUBTRAI O VALOR APOSTADO NO BRANCO
                                System.out.println("Cor resultado: " + re + "Acertou: R$" + saldo);
                            }
                            //VERIFICA SE APOSTA != RESULTADO (PRETO OU VERMELHO)
                            else if (!corEscolhida.equals(corResultado)) {
                                int b = 2;
                                xMartingale = xMartingale * b;
                                multiploMatingale = xMartingale;
                                //JA QUE APOSTA(PRETO OU VERMELHO) NAO SAIU PODE SAIR BRANCO. VERIFICA APOSTA BRANCO
                                if (corBranco.equals(corResultado)) {
                                    multiploMatingale = 1;
                                    saldo = saldo + ((xBranco * multiploMatingale) * apostaBranco);
                                    System.out.println("Cor resultado: " + re + "Acertou 14x: R$" + saldo);
                                } else if (!corBranco.equals(corResultado)) {
                                    if (xMartingale > 8) {
                                        multiploMatingale = 1;
                                        xMartingale = 1;
                                    }
                                    saldo = (saldo - apostaBranco * multiploMatingale) - valorAposta;
                                    System.out.println("Cor resultado: " + re + "Errou: R$" + saldo + " Martingale:" + multiploMatingale + " vlAposta:" + valorAposta + " apostaBranco:" + apostaBranco * multiploMatingale);
                                }//END BRANCO
                            } else if (!corResultado.equals(pretoTela) || !corResultado.equals(vermelhoTela)) {
                                System.out.println("Fora do jogo");
                            }
                        }

                        //COMO O VALOR DA APOSTA E MENOR DO QUE 2 ELE NAO APOSTA NAS BRANCAS
                        else if (valorAposta < 20) {
                            //VERIFICA SE APOSTA == COR RESULTADO (PRETO OU VERMELHO)
                            if (corEscolhida.equals(corResultado)) {
                                saldo = saldo + xPreto * valorAposta;//SE SUBTRAI O VALOR APOSTADO NO BRANCO
                                System.out.println("Cor resultado: " + re + "Acertou: R$" + saldo);
                            }
                            //VERIFICA SE APOSTA != RESULTADO (PRETO OU VERMELHO)
                            else if (!corEscolhida.equals(corResultado)) {
                                saldo = saldo - valorAposta;
                                System.out.println("Cor resultado: " + re + "Errou: R$" + saldo);
                            }
                        }
                        verificaResultadoAposta = 1;
                        verificaEscolhaAposta = 0;
                        verificaEstadoDoBotaoRealizaAposta = 0;
                    }//FIM DO IF
                }//FIM VERIFICACAO RESULTADO APOSTA
                  verificaResultadoAposta = 0;
        }//FIM LACO PRINCIPAL
    }//FIM EXECUTE

    private int verificaValorAposta(double valorAposta, int todasCoresParaEscolherAposta, Random apostaAleatoria) {
        if(valorAposta >=20){
            todasCoresParaEscolherAposta = apostaAleatoria.nextInt(3)+1;
        }

        if(valorAposta <20){
            todasCoresParaEscolherAposta = apostaAleatoria.nextInt(2)+1;
        }
        return todasCoresParaEscolherAposta;
    }

//======================================================================================================================
    //VERIFICA CORES
    public Object verificaCores(int X, int Y, int position) throws AWTException, InterruptedException {
        Color corColetada = robot.getPixelColor(X,Y);

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

    //METODO QUE VERIFICA A COR DO BOTAO PARA REALIZAR A APOSTA
    public void realizaClickBotaoAposta() throws InterruptedException {
        Bot bot = new Bot();
        if(bot.coordenadasBotaoConfirmaAposta.equals(rgbBotaoConfirmaAposta)){
//            System.out.println("Clicando no botao de aposta");
            robot.mouseMove(430, 400);//BOTAO MOVE EFETUA APOSTA
//            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);//APERTA BOTAO ESQUERDO DO MOUSE
//            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);//SOLTA BOTAO ESQUERDO DO MOUSE
            TimeUnit.SECONDS.sleep(16);
//            System.out.println("16 segundos");
            robot.mouseMove(336, 469);//BOTAO
            verificaEstadoDoBotaoRealizaAposta =1;
        }
    }

    //PRINT DA TELA
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
