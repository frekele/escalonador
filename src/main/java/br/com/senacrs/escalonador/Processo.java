package br.com.senacrs.escalonador;

import java.util.ArrayList;
import java.util.List;


public class Processo {

    private List<Integer> temposExecucao = new ArrayList<>();
    private List<Integer> temposDispositivo = new ArrayList<>();
    private List<String> timeLineProcesso = new ArrayList<>();
    private int sobraTimeSlice;
    private int tempo;
    private int etapaAtual;
    private boolean terminado;

    public boolean isTerminado() {
        return terminado;
    }

    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
    }

    public int getEtapaAtual() {
        return etapaAtual;
    }

    public void setEtapaAtual(int etapaAtual) {
        this.etapaAtual = etapaAtual;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getSobraTimeSlice() {
        return sobraTimeSlice;
    }

    public void setSobraTimeSlice(int sobraTimeSlice) {
        this.sobraTimeSlice = sobraTimeSlice;
    }

    public Processo() {
        this.sobraTimeSlice = 0;
        this.tempo = 0;
        this.etapaAtual = 0;
        this.terminado = false;
    }

    public Integer getQtdDeTempos() {
        return this.temposExecucao.size();
    }

    public Integer getTimeLineSize() {
        return timeLineProcesso.size();
    }


    public List<String> getTimeLineProcesso() {
        return timeLineProcesso;
    }

    public String getTimeLinePos(int pos) {
        return timeLineProcesso.get(pos);
    }

    public void setTimeLine(String valor) {
        this.timeLineProcesso.add(valor);
        this.tempo++;
    }

    public Integer getTempoDispositivo(int pos) {
        return temposDispositivo.get(pos);
    }

    public void setTempoDispositivo(int valor) {
        this.temposDispositivo.add(valor);
    }

    public void setTempoDispPos(int pos, int valor) {
        this.temposDispositivo.set(pos, valor);
    }

    public Integer getTempoExecucao(int pos) {
        return temposExecucao.get(pos);
    }

    public void setTempoExecucao(int valor) {
        this.temposExecucao.add(valor);
    }

    public void setTempoExecucaoPos(int pos, int valor) {
        this.temposExecucao.set(pos, valor);
    }

}
