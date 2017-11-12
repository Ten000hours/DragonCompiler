package com.symbols;

import inter.Id;

import java.util.Hashtable;

import lexer.Token;

public class Env {

	private Hashtable table;
	protected Env prev;

	public Env(Env v) {
		table = new Hashtable();
		prev = v;

	}

	public void put(Token k, Id i) {
		table.put(k, i);

	}

	public Id get(Token k) {
		for (Env e = this; e != null; e = e.prev) {
			Id found = (Id) (e.table.get(k));
			if (found != null)
				return found;

		}
		return null;
	}
}
