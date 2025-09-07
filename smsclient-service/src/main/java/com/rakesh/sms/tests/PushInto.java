package com.rakesh.sms.tests;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class PushInto {

	public static void main(String[] args) throws Exception {

		Queue<String> queue = new LimitedQueue<String>();

		queue.add("1");
		queue.add("2");
		queue.add("3");
		queue.add("4");

		System.out.println(queue.size());
		System.out.println("Peek: " + queue.peek());

		queue.add("5");
		System.out.println(queue.size());
		System.out.println("Peek: " + queue.peek());

		queue.add("6");
		System.out.println(queue.size());
		System.out.println("Peek: " + queue.peek());

		queue.add("7");
		System.out.println(queue.size());
		System.out.println("Peek: " + queue.peek());

		queue.add("8");
		System.out.println(queue.size());
		System.out.println("Peek: " + queue.peek());

	}// End Of Main

	static class LimitedQueue<E> extends ArrayBlockingQueue<E> {

		private static final long serialVersionUID = 1L;
		private static final int QUEUE_LIMIT = 5;
		private Integer limit;

		public LimitedQueue() {
			super(QUEUE_LIMIT);
			this.limit = QUEUE_LIMIT;
		}// End Of Constructor

		public LimitedQueue(Integer limit) {
			super(limit);
			this.limit = limit;
		}// End Of Constructor

		@Override
		public boolean add(E e) {

			while (super.size() >= this.limit) {
				super.poll();
			} // End Of Loop

			return super.add(e);

		}// End Of Method

		@Override
		public int size() {
			return super.size();
		}// End Of Method

	}// End Of Class

}