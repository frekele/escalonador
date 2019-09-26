
package br.com.senacrs.escalonador;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Escalonador {
    public static void main(String[] args) throws FileNotFoundException {
        Escalonador escalonador = new Escalonador();
        escalonador.start();
    }

    private void start() throws FileNotFoundException {

        //declaracao das variaveis globais do programa
        int timeSlice = 0;
        int quantidadeProcessos = 0;
        int aux = 0;
        int timeLineSize = 0;
        int qtdTemposMax = 0;
        Processo processo;
        List<Processo> listaProcessos = new ArrayList<>();

        //declaracao da classe de leitura do java
        Scanner e = new Scanner(System.in);

        //escreve o cabecalho do programa
        System.out.println("ESCALONADOR DE CURTO PRAZO");

        //lê o timeslice que o escalonador tera
        do {
            //escreve na tela TimeSlice
            System.out.print("Time Slice: ");

            //guarda na viariavel timeSlice o valor digitado 
            timeSlice = e.nextInt();

            //verificacao do tamanho do time slice
            if (timeSlice > 4 || timeSlice < 1) {
                System.out.println("O time Slice deve ser um valor entre 1 e 4!");
            }
            //repete ateh que o valor do time slice seja entre 1 e 4
        } while (timeSlice > 4 || timeSlice < 1);

        //lê a quantidade de processos que o escalonador terá terá
        System.out.print("Quantidade de Processos: ");
        quantidadeProcessos = e.nextInt();
        System.out.println("----");

        //inicio do loop de Leitura dos dados de processo
        for (int i = 1; i < quantidadeProcessos + 1; i++) {

            //instancia Processo vazio
            processo = new Processo();

            //escreve o nome do processo em tela
            System.out.println("P" + i);

            do {
                System.out.print("TM: ");
                processo.setTempoExecucao(e.nextInt());
                System.out.print("DS: ");
                aux = e.nextInt();
                processo.setTempoDispositivo(aux);
                if (processo.getQtdDeTempos() > qtdTemposMax)
                    qtdTemposMax = processo.getQtdDeTempos();
            }
            while (aux != 0);

            listaProcessos.add(processo);

            System.out.println("----");

        }  //termino da leitura dos dados
        System.out.println(" ");

        //variaveis para a execucao do escalonador
        int tempo = 0;
        int posicao = 0;
        int qtdTempos = 0;
        int sobraTimeSlice = 0;
        boolean bloqueadoTimeSlice = false;
        int procAtual = 0;
        int tempoExecucao = 0;
        int etapaAtual = 0;

        //início da execucao do escalonador
        //Enquanto verdadeiro
        while (true) {
            //Enquanto verdadeiro
            while (true) {

                //guarda numa variavel/objeto o primeiro processo
                processo = listaProcessos.get(procAtual);

                //guarda numa variavel o tempo de execucao do processo atual
                etapaAtual = processo.getEtapaAtual();

                //guarda numa variavel a quantidade maxima de tempos que esse o processo atual tem
                qtdTempos = processo.getQtdDeTempos();

                if (etapaAtual > (qtdTempos - 1)) {
                    if (procAtual < (quantidadeProcessos - 1))
                        procAtual++;
                    else procAtual = 0;

                    break;
                }

                tempoExecucao = processo.getTempoExecucao(etapaAtual);

                if (tempoExecucao <= 0) {

                    if (processo.getSobraTimeSlice() > 0) {
                        bloqueadoTimeSlice = true;
                        processo.setTempoExecucaoPos(etapaAtual, processo.getSobraTimeSlice());
                        tempoExecucao = processo.getTempoExecucao(etapaAtual);
                        sobraTimeSlice = processo.getSobraTimeSlice();
                        processo.setSobraTimeSlice(sobraTimeSlice - timeSlice);
                        if (processo.getSobraTimeSlice() < 0) processo.setSobraTimeSlice(0);

                    }
                }

                if (tempoExecucao > timeSlice) {
                    processo.setTempoExecucaoPos(etapaAtual, timeSlice);
                    processo.setSobraTimeSlice(tempoExecucao - timeSlice);
                    tempoExecucao = processo.getTempoExecucao(etapaAtual);
                }

                //O core de tudo
                if (tempoExecucao > 0) {
                    if (isAble(processo, procAtual, listaProcessos, processo.getTempo())) {
                        processo.setTimeLine(".");
                    } else {
                        tempoExecucao = tempoExecucao - 1;
                        processo.setTempoExecucaoPos(etapaAtual, (tempoExecucao));
                        processo.setTimeLine("x");
                    }
                    ;
                }

                // se acabou de executar o primeito TM por completo irá chamar o disco
                if (tempoExecucao == 0 && processo.getSobraTimeSlice() == 0) {
                    for (int v = 0; v < processo.getTempoDispositivo(etapaAtual); v++) {
                        if (isBlocked(processo, procAtual, listaProcessos, processo.getTempo())) {
                            processo.setTimeLine("b");
                            v--;
                        } else
                            processo.setTimeLine("d");
                    }

                    processo.setTempoDispPos(etapaAtual, 0);

                    processo.setEtapaAtual(processo.getEtapaAtual() + 1);
                }

                if (procAtual == (quantidadeProcessos - 1) && tempoExecucao == 0 && processo.getSobraTimeSlice() == 0) {
                    procAtual = 0;
                    break;
                }
                ;

                if (tempoExecucao == 0) {
                    if (procAtual < (quantidadeProcessos - 1))
                        procAtual++;
                    else procAtual = 0;
                }

            } //while true

            //System.out.println("processo.getQtdDeTempos(): " + processo.getQtdDeTempos() + " - processo.getEtapaAtual(): " + processo.getEtapaAtual());

            //verifica e seta para terminado o processo caso ele tenha chegado a quantidade maxima de tempos
            if (processo.getQtdDeTempos() == processo.getEtapaAtual())
                processo.setTerminado(true);

            Processo processoTeste;
            boolean teste = true;
            for (int i = 0; i < quantidadeProcessos; i++) {
                processoTeste = listaProcessos.get(i);
                teste = processoTeste.isTerminado();
                if (!teste) break;
            }
            if (teste) break;


        } //while true

        //inicio da escrita do resultado do escalonador em tela e no arquivo
        for (int i = 0; i < quantidadeProcessos; i++) {
            processo = listaProcessos.get(i);
            if (processo.getTempo() > timeLineSize)
                timeLineSize = processo.getTempo();

            System.out.print("P" + (i + 1) + ": ");

            for (int x = 0; x < processo.getTimeLineSize(); x++) {

                System.out.print(processo.getTimeLinePos(x));
            }

            System.out.println("");
        }

        System.out.print("    ");
        for (int x = 0; x < timeLineSize; x++) {
            if (x < 10) {
                System.out.print(x);

            } else {
                System.out.print(x % 10);
            }
        }
        System.out.println("");

        //abre arquivo dipositivo.log para escrita de seu resultado
        try (PrintWriter out = new PrintWriter("logger-dispositivo.log")) {
            out.println("Seg  processo");
            for (int x = 0; x < timeLineSize; x++) {

                for (int i = 0; i < quantidadeProcessos; i++) {
                    processo = listaProcessos.get(i);
                    if (x < processo.getTimeLineSize() - 1) {
                        if ("d".equals(processo.getTimeLinePos(x))) {
                            if (x < 10) out.print("0" + x);
                            else out.print(x);
                            out.print("   ");
                            out.println("P" + (i + 1));
                        }
                    }
                }
            }
        }

        //abre arquivo processos.log para escrita do resultado
        try (PrintWriter out = new PrintWriter("logger-processos.log")) {
            out.print("Seg ");
            for (int i = 0; i < quantidadeProcessos; i++) {
                out.print("P" + (i + 1) + "   ");
            }
            out.println("");

            for (int x = 0; x < timeLineSize; x++) {
                if (x < 10)
                    out.print("0" + x);
                else out.print(x);

                out.print("  ");

                for (int i = 0; i < quantidadeProcessos; i++) {
                    processo = listaProcessos.get(i);
                    if (x < processo.getTimeLineSize() - 1) {
                        if ("x".equals(processo.getTimeLinePos(x)))
                            out.print("exec ");
                        if (".".equals(processo.getTimeLinePos(x)))
                            out.print("apto ");
                        if ("d".equals(processo.getTimeLinePos(x)))
                            out.print("disp ");
                        if ("b".equals(processo.getTimeLinePos(x)))
                            out.print("disp ");
                    }
                    if (x == processo.getTimeLineSize() - 1)
                        out.print("endp ");
                    if (x > processo.getTimeLineSize() - 1)
                        out.print("---- ");
                }
                out.println("");

            }
        }


    }   //fim do metodo main


    //metodo criado para verificar se o processo está apto comparando com os outros processos
    private static boolean isAble(Processo processo, int processoAnalisado, List<Processo> listaProcessos,
                                  int tempoAnalisado) {

        Processo procComparado;
        List<String> timeLineProcess;
        //System.out.println("tempoAnalisado: " + tempoAnalisado);

        for (int i = 0; i < listaProcessos.size(); i++) {
            if (i != processoAnalisado || tempoAnalisado != 0) {

                procComparado = listaProcessos.get(i);
                timeLineProcess = procComparado.getTimeLineProcesso();

                if (!timeLineProcess.isEmpty() && timeLineProcess.size() > tempoAnalisado) {

                    if ("x".equals(timeLineProcess.get(tempoAnalisado))) {
                        //System.out.println("Apto"); 
                        return true;
                    }
                }
            }
        }

        //System.out.println("Livre");
        return false;
    }


    //metodo criado para verificar se o processo está bloqueado comparando com os outros processos
    private static boolean isBlocked(Processo processo, int processoAnalisado, List<Processo> listaProcessos,
                                     int tempoAnalisado) {

        Processo procComparado;
        List<String> timeLineProcess;
        //System.out.println("tempoAnalisado: " + tempoAnalisado);

        for (int i = 0; i < listaProcessos.size(); i++) {
            if (i != processoAnalisado || tempoAnalisado != 0) {

                procComparado = listaProcessos.get(i);
                timeLineProcess = procComparado.getTimeLineProcesso();

                if (!timeLineProcess.isEmpty() && timeLineProcess.size() > tempoAnalisado) {

                    if ("d".equals(timeLineProcess.get(tempoAnalisado))) {
                        //System.out.println("Bloqueado"); 
                        return true;
                    }
                }
            }
        }

        //System.out.println("Disco");
        return false;
    }
}
