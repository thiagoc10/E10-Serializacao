//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.io.Serializable;

public abstract class Conta implements ITaxas, Serializable {
    private int numero;
    private Cliente dono;
    private double saldo;
    protected double limite;
    private List<Operacao> operacoes;
    private static int totalContas = 0;
    private static final long serialVersionUID = 1L;
    private int nConta, agencia;

    public Conta(int numero, Cliente dono, double saldo, double limite, int agencia) {
        this.numero = numero;
        this.dono = dono;
        this.saldo = saldo;
        this.limite = limite;
        this.operacoes = new ArrayList();
        this.agencia = agencia;
        ++totalContas;
    }

    public boolean sacar(double valor) {
        if (valor >= 0.0 && valor <= this.limite) {
            this.saldo -= valor;
            this.operacoes.add(new OperacaoSaque(valor));
            return true;
        } else {
            return false;
        }
    }

    public void depositar(double valor) throws ArithmeticException {
        if (valor < 0.0) {
            throw new ArithmeticException("Erro. Valor negativo depositado.");
        } else {
            this.saldo += valor;
            this.operacoes.add(new OperacaoDeposito(valor));
        }
    }

    public boolean transferir(Conta destino, double valor) {
        boolean valorSacado = this.sacar(valor);
        if (valorSacado) {
            destino.depositar(valor);
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        String var10000 = this.dono.toString();
        return var10000 + "\n---\nnumero=" + this.numero + "\nsaldo=" + this.saldo + "\nlimite=" + this.limite;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof Conta) {
            Conta conta = (Conta)o;
            return this.numero == conta.numero;
        } else {
            return false;
        }
    }

    public void imprimirExtrato(int modo) {
        List<Operacao> operacoesParaExtrato = new ArrayList(this.operacoes);
        if (modo == 1) {
            Collections.sort(operacoesParaExtrato);
        }

        System.out.println("======= Extrato Conta " + this.numero + "======");
        Iterator var3 = operacoesParaExtrato.iterator();

        while(var3.hasNext()) {
            Operacao atual = (Operacao)var3.next();
            System.out.println(atual);
        }

        System.out.println("====================");
    }

    public void imprimirExtratoTaxas() {
        System.out.println("=== Extrato de Taxas ===");
        System.out.printf("Manutenção:\t%.2f\n", this.calcularTaxas());
        double totalTaxas = this.calcularTaxas();
        Iterator var3 = this.operacoes.iterator();

        while(var3.hasNext()) {
            Operacao atual = (Operacao)var3.next();
            totalTaxas += atual.calcularTaxas();
            System.out.printf("%c:\t%.2f\n", atual.getTipo(), atual.calcularTaxas());
        }

        System.out.printf("Total:\t%.2f\n", totalTaxas);
    }
public int getAgencia(){
        return this.agencia;
}
    public int getNumero() {
        return this.numero;
    }

    public Cliente getDono() {
        return this.dono;
    }

    public double getSaldo() {
        return this.saldo;
    }

    public double getLimite() {
        return this.limite;
    }

    public static int getTotalContas() {
        return totalContas;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setDono(Cliente dono) {
        this.dono = dono;
    }

    public abstract void setLimite(double var1);
    public static Conta getConta(int agencia, int nConta) {
        Conta acc = null;
        try (FileInputStream fileIn = new FileInputStream("contas" + agencia + "_" + nConta + ".ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            acc = (Conta) in.readObject();
        }
        catch (IOException e) {

            System.err.println("Erro ao ler a conta " + e.getMessage());
        } catch (ClassNotFoundException e) {

            System.err.println("Classe não encontrada" + e.getMessage());
        }
        return acc;
        // função que desserializa uma conta de um arquivo
    }
    public void CarregaConta() {

        try (FileOutputStream fileOut = new FileOutputStream("contas/" + agencia + "_" + numero + ".ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) { out.writeObject(this);
        } catch (IOException e) {
            System.err.println("Erro ao carregar a conta " + e.getMessage());
        }
    }
}
